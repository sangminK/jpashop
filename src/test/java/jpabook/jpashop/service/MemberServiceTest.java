package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // junit 실행할 때, 스프링이랑 엮어서 실행할래
@SpringBootTest     // 스프링 부트를 띄운 상태로 테스트를 하기 위해
@Transactional      // 테스트 끝나고 rollback
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    //@Rollback(false)
    @Test
    public void 회원가입() throws Exception {
        // given : 주어졌을 때
        Member member = new Member();
        member.setName("smkim");

        // when : 이렇게 하면
        Long saveId = memberService.join(member);

        // then : 이렇게 된다
        // em.flush(); // insert 문을 직접 보고 싶은 경우 사용
        assertEquals(member, memberRepository.findOne(saveId));
        // 같은 트랜잭션(영속성) 안에서 ID가 같으면 같은 영속성 컨텍스트에서 하나로 관리된다.
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2);    // 예외가 발생해야 한다!!!

        // then
        fail("예외가 발생해야 한다.");   // fail() : 코드가 여기까지 오면 안된다.
    }

}