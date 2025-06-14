package com.recommand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	@RequestMapping(value = {
		    "/{path:^(?!api|static|js|css|images|favicon\\.ico|uploads|logo192\\.png|manifest\\.json|index\\.html).*$}",
		    "/{path:^(?!api|static|js|css|images|favicon\\.ico|uploads|logo192\\.png|manifest\\.json|index\\.html).*$}/**"
		})
    public String redirect() {
        return "forward:/index.html";
    }
}
