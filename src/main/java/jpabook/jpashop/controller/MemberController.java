package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        /*
        * Member 엔티티를 바로 사용하지 않고 MemberForm을 따로 만드는 이유
        * - form과 엔티티를 합쳐서 쓰면 엔티티가 너무 지저분해 지고
        *   form과 엔티티의 요구사항이 다를 수 있다
        *   form 은 화면에 fit되게 맞춰 쓰는 것이 좋다
        *   특히나 실무에서는 더 복잡하기 때문에 분리하는 추천(유지보수도 더 어려워진다)
        *   엔티티는 최대한 순수하게 유지
        * */

        /*
        * API를 만들 때는 절대 엔티티를 외부로 반환하면 안된다
        * - 엔티티에 필드를 하나 추가하면, 1) API 스펙이 변해버리고 2) 필드가 그대로 노출되는 문제
        *   템플릿 엔진은 서버사이드이기 때문에 선택적으로 사용할 수도 있다
        * */

        if(result.hasErrors()) {
            return "members/createMemberForm";    // 에러 발생시 여기로
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
