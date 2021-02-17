package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // readOnly = false : default
@RequiredArgsConstructor
public class MemberService {

    /*
    * 방법1 필드에 @Autowired > 변경 불가한 단점
    * 방법2 setter > 런타임에 변경 가능해져 버림..
    * 방법3 생성자 > 변경 가능 & 생성할 때 끝남
    * 방법4 lombok @AllArgsConstructor : 전부 만들어줌
    * 방법5 lombok @RequiredArgsConstructor : final 필드만 만들어줌
    * */

    // final : 값세팅 안하면 에러 > 컴파일 시점에 체크 가능해짐
    private final MemberRepository memberRepository;

    // @Autowired 선언 안해도, 생성자 1개면 스프링이 알아서 주입해줌
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /*
    * 회원 가입
    * */
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 실무에서는 was가 여러개 뜨기 때문에 이런 방식의 valid 만으로는 안됨
    // 최후의 처리로 필드에 unique 처리까지 해야
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전제 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

/*
* @Transactional 의 경우, 조회만 하는 곳에서는 readOnly = true 를 해주면
* jpa가 조회하는 곳에서 좀 더 성능 최적화
* */

/*
* 서비스 레벨의 @Transactional은 readOnly 의 법칙은 없고
* 많이 쓰는 것을 기본으로 하고 예외의 경우만 따로 명시
* ex. read가 많으면 서비스 레벨에 @Transactional(readOnly = true) 하고
* 예외의 경우에 @Transactional 로 명시
* */