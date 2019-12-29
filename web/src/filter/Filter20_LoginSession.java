package filter;//package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//验证过滤器
//@WebFilter(filterName = "Filter 20",urlPatterns = {"/*"})
public class Filter20_LoginSession implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //打印过滤器开始
        System.out.println("Filter 20 - LoginSessionFilter begin!");
        //将请求和回应转换成Http类型
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers","Content-Type, api_key, Authorization");
        response.setHeader("Access-Control-Allow-Origin","*");
        //获取请求的session并将自动创建关闭
        HttpSession session = request.getSession(false);
        //获取当前请求的路径
        String path = request.getRequestURI();
        //如果路径中包含。。则对其进行放行
        if (path.contains("/managerLogin") ||
                path.contains("/logout") ||
                path.contains("/userLogin"))
        {
            //放行，打印过滤器结束
            chain.doFilter(req, resp);
            System.out.println("Filter 20 - LoginSessionFilter ends!");
        }if (path.contains("/card.ctl") ||
                path.contains("/electricity.ctl") ||
                path.contains("/water.ctl") ||
                path.contains("/payment.ctl") ||
                path.contains("/record.ctl"))
        {
            if (session != null && session.getAttribute("currentUser") != null) {
                //if login ,then go
                chain.doFilter(req, resp);
                System.out.println("Filter 20 - LoginSessionFilter ends!");
            } else {
                //如果没有登录则提示登录并不放行
                response.getWriter().println("您没有登录，请登录");
            }
        }
        if (path.contains("/investCard.ctl") ||
                path.contains("/investSlogin.ctl") ||
                path.contains("/makeUpCard.ctl") ||
                path.contains("/slogin.ctl") ||
                path.contains("/mcard.ctl") ||
                path.contains("/student.ctl") ||
                path.contains("/MRecord.ctl")){
            if (session != null && session.getAttribute("currentManager") != null) {
                //if login ,then go
                chain.doFilter(req, resp);
                System.out.println("Filter 20 - LoginSessionFilter ends!");
            } else {
                //如果没有登录则提示登录并不放行
                response.getWriter().println("您没有登录，请登录");
            }

        }
        if (path.contains("/details.ctl")){
            if (session != null && (session.getAttribute("currentUser") != null ||
                    session.getAttribute("currentUser") != null)) {
                //if login ,then go
                chain.doFilter(req, resp);
                System.out.println("Filter 20 - LoginSessionFilter ends!");
            } else {
                //如果没有登录则提示登录并不放行
                response.getWriter().println("您没有登录，请登录");
            }

        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
