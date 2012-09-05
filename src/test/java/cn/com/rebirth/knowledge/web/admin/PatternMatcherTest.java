package cn.com.rebirth.knowledge.web.admin;

import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

public class PatternMatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PatternMatcher patternMatcher = new AntPathMatcher();

		boolean b = patternMatcher.matches("/system/sysUser/{id}", "/system/sysUser/1111");
		System.out.println(b);
		
		org.springframework.util.AntPathMatcher antPathMatcher=new org.springframework.util.AntPathMatcher();
		b=antPathMatcher.match("/system/sysUser/{id}/aaaa/{aa}", "/system/sysUser/1111/aaaa/dddd");
		System.out.println(b);
	}

}
