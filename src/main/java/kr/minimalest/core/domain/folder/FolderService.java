package kr.minimalest.core.domain.folder;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.folder.dto.FolderCreateRequest;
import kr.minimalest.core.domain.folder.dto.FolderCreateResponse;
import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.folder.dto.FolderWithPost;
import kr.minimalest.core.domain.post.Post;
import kr.minimalest.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final PostRepository postRepository;
    private final ArchiveRepository archiveRepository;

    @Transactional
    public FolderCreateResponse create(FolderCreateRequest folderCreateRequest, String author) {
        Archive archive = archiveRepository.findByAuthor(author)
                .orElseThrow(() -> new EntityNotFoundException("해당 아카이브는 존재하지 않습니다!"));
        if (folderRepository.findByNameAndArchiveAuthor(folderCreateRequest.getName(), author).isPresent()) {
            // 이미 존재하는 이름의 폴더라면 예외 발생
            throw new IllegalArgumentException("해당 폴더 이름이 이미 존재합니다!");
        };
        Folder folder = folderCreateRequest.toEntity(archive);
        folderRepository.save(folder);
        return new FolderCreateResponse(FolderView.fromEntity(folder));
    }

    @Transactional
    public void updateFolderStatus(String author, Long folderId, FolderStatus status) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 폴더가 존재하지 않습니다!"));
        if (status == FolderStatus.DELETED) {
            // 삭제해야할 땐 공개된 폴더가 최소 1개인지 확인
            if (folderRepository.countByAuthor(author, FolderStatus.ACTIVE) <= 1) {
                throw new IllegalStateException("폴더는 최소 1개 이상이여야 합니다!");
            }
        }
        folder.updateStatus(status);
    }

    @Transactional(readOnly = true)
    public FolderView validateFolder(Long id) {
        Optional<Folder> optionalFolder = folderRepository.findById(id);
        Folder folder = optionalFolder.orElseThrow(() -> new EntityNotFoundException("해당 폴더가 존재하지 않습니다!"));
        return FolderView.fromEntity(folder);
    }

    @Transactional
    public List<FolderView> getFolderTree(String author) {
        List<FolderView> flatFolders = getFlatFolders(author);

        // 각 폴더의 id를 매핑시킵니다.
        Map<Long, FolderView> folderIndexMap = mappingFolderIndex(flatFolders);

        // 트리를 구축합니다.
        return constructTree(flatFolders, folderIndexMap);
    }

    @Transactional
    public List<FolderView> getFlatFolders(String author) {
        List<Folder> folders = folderRepository.findAllByArchiveAuthor(author);

        List<FolderView> flatFolders = folders.stream()
                .map((folder) -> (
                        new FolderView(
                                folder.getId(),
                                folder.getName(),
                                folder.getDepth(),
                                folder.getParent() == null ? null : folder.getParent().getId(),
                                folder.getParent() == null,
                                folder.getChildren().isEmpty()
                        )))
                .toList();

        // Post -> FolderWithPost 매핑
        List<Post> posts = postRepository.findAllByArchiveAuthor(author);
        List<FolderWithPost> folderWithPosts = posts.stream()
                .map((post) -> (new FolderWithPost(post.getId(), post.getFolder().getId(), post.getTitle())))
                .toList();

        // 빈 폴더에 Post 정보를 부착합니다.
        Map<Long, List<FolderWithPost>> postMap = new HashMap<>();
        for (FolderWithPost folderWithPost : folderWithPosts) {
            // 만약 해당 폴더 id에 해당하는 value 없다면 빈 리스트를 반환, 존재한다면 기존 value 반환 후 포스트 추가
            postMap.computeIfAbsent(folderWithPost.getFolderId(), k -> new ArrayList<>()).add(folderWithPost);
        }
        for (FolderView flatFolder : flatFolders) {
            // 해당 폴더 key에 맞는 포스트 리스트 부착
            flatFolder.setPosts(postMap.getOrDefault(flatFolder.getId(), new ArrayList<>()));
        }

        return flatFolders;
    }

    private static Map<Long, FolderView> mappingFolderIndex(List<FolderView> flatFolders) {
        Map<Long, FolderView> folderIndexMap = new HashMap<>();

        for (FolderView folderView : flatFolders) {
            folderIndexMap.put(folderView.getId(), folderView);
        }

        return folderIndexMap;
    }

    private static List<FolderView> constructTree(List<FolderView> flatFolders, Map<Long, FolderView> folderIndexMap) {
        List<FolderView> folderRoot = new ArrayList<>();

        for (FolderView folderView : flatFolders) {
            // folderView가 루트라면
            if (folderView.isRoot()) {
                // 루트에 추가
                folderRoot.add(folderView);
            } else {
                // 해당 부모의 자식에 추가
                FolderView parentFolderView = folderIndexMap.get(folderView.getParentId());
                parentFolderView.getChildren().add(folderView);
            }
        }

        return folderRoot;
    }
}
