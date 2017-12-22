package sec.project.controller;

import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }
	
	@RequestMapping(value = {"/robots", "/robot", "/robot.txt", "/robots.txt", "/null"})
	public void robots(HttpServletResponse response) { 
		InputStream resourceAsStream = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			resourceAsStream = classLoader.getResourceAsStream("robots.txt");
			response.addHeader("Content-disposition", "filename=robots.txt");
			response.setContentType("text/plain");
			IOUtils.copy(resourceAsStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {

		}
	}
	
	@RequestMapping(value = "/done", method = RequestMethod.GET)
    public String loadDone(Model model) {
		List<Signup> signups = signupRepository.findAll();
		model.addAttribute("signups", signups);
        return "done";
    }

	
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String ccnr) {
        signupRepository.save(new Signup(name, address, ccnr));
        return "redirect:/done";
    }
	

}
