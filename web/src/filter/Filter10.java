//201802104009张欣茹
package filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 10",urlPatterns = {"/*"})
public class Filter10 implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 -encoding begins");
        HttpServletRequest request = (HttpServletRequest)req;//ServletRequest中无getRequestURI()方法
        HttpServletResponse response = (HttpServletResponse)resp;
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers","Content-Type, api_key, Authorization");
        response.setHeader("Access-Control-Allow-Origin","*");
        String path = request.getRequestURI();
        if (path.contains("/myapp")|| path.contains("/login")) {
            //如果发现是css或者js文件，直接放行
            chain.doFilter(req, resp);
            System.out.println("Filter 1 - date ends");
        }
        else {
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("设置响应字符编码为UTF-8");
            String method = request.getMethod();
            if ("POST-PUT".contains(method)) {
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
                System.out.println("设置请求字符编码格式为UTF-8");
            }
            chain.doFilter(req, resp);//执行其他过滤器，如果过滤器已经执行完毕，则执行原请求
            System.out.println("Filter 10 -encoding ends");
        }
    }
}
