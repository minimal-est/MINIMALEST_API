package kr.minimalest.core.domain.folder;

import kr.minimalest.core.domain.folder.dto.FolderView;
import kr.minimalest.core.domain.folder.dto.FolderWithPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    @Transactional
    public List<FolderView> getFolderTree(String author) {
        List<FolderView> flatFolders = getFlatFolders(author);

        // 각 폴더의 id를 매핑시킵니다.
        Map<Long, FolderView> folderIndexMap = mappingIndex(flatFolders);

        // 트리를 구축합니다.
        return constructTree(flatFolders, folderIndexMap);
    }

    private List<FolderView> getFlatFolders(String author) {
        List<FolderView> flatFolders = folderRepository.findFolderViewByAuthor(author);

        // 빈 폴더에 Post 정보를 부착합니다.
        for (FolderView flatFolder : flatFolders) {
            List<FolderWithPost> posts = folderRepository.findPostsById(flatFolder.getId());
            flatFolder.setPosts(posts);
        }

        return flatFolders;
    }

    private static Map<Long, FolderView> mappingIndex(List<FolderView> flatFolders) {
        Map<Long, FolderView> folderIndexMap = new HashMap<>();

        for (FolderView folderView : flatFolders) {
            folderIndexMap.put(folderView.getId(), folderView);
        }

        return folderIndexMap;
    }

    private static List<FolderView> constructTree(List<FolderView> flatFolders, Map<Long, FolderView> folderIndexMap) {
        List<FolderView> folderTree = new ArrayList<>();

        for (FolderView folderView : flatFolders) {
            if (folderView.isRoot()) {
                folderTree.add(folderView);
            } else {
                FolderView parentFolderView = folderIndexMap.get(folderView.getParentId());
                parentFolderView.getChildren().add(folderView);
            }
        }

        return folderTree;
    }
}
