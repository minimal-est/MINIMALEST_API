package kr.minimalest.core.util;

import jakarta.annotation.PostConstruct;
import kr.minimalest.core.domain.archive.Archive;
import kr.minimalest.core.domain.archive.ArchiveRepository;
import kr.minimalest.core.domain.folder.Folder;
import kr.minimalest.core.domain.folder.FolderRepository;
import kr.minimalest.core.domain.member.Member;
import kr.minimalest.core.domain.member.MemberRepository;
import kr.minimalest.core.domain.member.MemberUtils;
import kr.minimalest.core.domain.profile.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@org.springframework.context.annotation.Profile("dev2")
public class TestDataInput {

    private final MemberRepository memberRepository;
    private final ArchiveRepository archiveRepository;
    private final FolderRepository folderRepository;

    @PostConstruct
    @Transactional
    public void insertData() {
        Member member = Member.builder()
                .email("31n5ang@gmail.com")
                .encPassword(MemberUtils.encodePassword("password"))
                .username("쿵야")
                .build();

        Profile profile = Profile.builder()
                .profileImageUrl("http://localhost:8080/api/file/proxy?key=image/5aa1c8f9-8fd8-414f-8e92" +
                        "-45af7a215034.jpeg")
                .member(member).build();

        member.setProfile(profile);

        memberRepository.save(member);

        Archive archive = Archive.builder()
                .author("31n5ang")
                .mainTitle("개발 아카이브")
                .subTitle("송작가의 아카이브입니다.")
                .build();

        member.addArchive(archive);
        archiveRepository.save(archive);


        Folder folder = new Folder("재밌는 스프링 시리즈", null, archive);
        Folder folder1 = new Folder("백엔드 프로그래머 도전기", null, archive);
        Folder folder2 = new Folder("운동 기록", null, archive);
        Folder folder3 = new Folder("무제", null, archive);
        Folder folder4 = new Folder("쿵야의 레스토랑즈", null, archive);
        Folder folder5 = new Folder("만두 찜쪄먹는 방법", null, archive);

        folder2.setParent(folder);
        folder3.setParent(folder);
        folder5.setParent(folder3);

        folderRepository.save(folder);
        folderRepository.save(folder1);
        folderRepository.save(folder4);

//        String content =
//                    """
//                    안녕하세요, 반갑습니다
//
//                    이 녀석을 터쳐볼까요?
//
//                    완전 터쳐야겠습니다.<br />
//
//                    *저는 이해가 됩니다*
//
//                    **하이하이**
//
//                    <br>엎어버리기
//                    <h3>넘어뜨리기</h3>
//                    <script>끝장내기</script>
//                    """;
//        Post post = new Post(
//                null,
//                "쿵야 엎어뜨리기",
//                content,
//                1L,
//                null,
//                archive,
//                folder5,
//                false,
//                null,
//                PostStatus.PUBLISHED,
//                PostRole.NONE
//        );
//
//        postRepository.save(post);
    }

    @PostConstruct
    @Transactional
    public void insertData2() {
        Member member = Member.builder()
                .email("31n5ang@gmail.com2")
                .encPassword(MemberUtils.encodePassword("password"))
                .username("쿵야2")
                .build();

        Profile profile = Profile.builder()
                .profileImageUrl("http://localhost:8080/api/file/proxy?key=image/5aa1c8f9-8fd8-414f-8e92" +
                        "-45af7a215034.jpeg")
                .member(member).build();

        member.setProfile(profile);

        memberRepository.save(member);

        Archive archive = Archive.builder()
                .author("송작가")
                .mainTitle("개발 아카이브인데요")
                .subTitle("송작가의 아카이브입니다.")
                .build();

        member.addArchive(archive);
        archiveRepository.save(archive);


        Folder folder1 = new Folder("백엔드 프로그래머 도전기", null, archive);

        folderRepository.save(folder1);
    }
}
