package sms.sample.jpa.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sample/jpa")
    public @ResponseBody String jpaPage() {
        repository.save(new Customer("Seo", "seunghyun"));
        repository.save(new Customer("Kim", "minwook"));
        repository.save(new Customer("Choi", "seunghyun"));

        return repository.findByLastName("seunghyun").toString();
    }
}
