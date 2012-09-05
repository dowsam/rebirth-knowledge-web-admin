package cn.com.rebirth.knowledge.web.admin;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.settings.ThreadSafeVariableSettings;
import cn.com.rebirth.core.inject.Injector;
import cn.com.rebirth.core.inject.ModulesBuilder;
import cn.com.rebirth.core.settings.SettingsModule;

public class InjectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModulesBuilder modulesBuilder = new ModulesBuilder();
		modulesBuilder.add(new SettingsModule(ThreadSafeVariableSettings.settingsBuilder().put("aaa", "aaaa").build()));
		Injector injector = modulesBuilder.createInjector();
		Settings settings = injector.getInstance(Settings.class);
		Settings settings2 = ThreadSafeVariableSettings.settingsBuilder().put(settings).put("as", "111111").build();
		injector.injectMembers(settings2);
		System.out.println(settings.get("aaa"));
		settings.getAsMap().put("aaa", "3333");
		System.out.println(settings.get("aaa"));
		
		
		Settings settings3 = injector.getInstance(Settings.class);
		System.out.println(settings3.get("as"));
		A a = injector.getInstance(A.class);
		System.out.println(a);
		a = new A();
		injector.injectMembers(a);
		a = injector.getInstance(A.class);
		System.out.println(a.a);
		a.a = "33333";
		injector.injectMembers(a);
		System.out.println(a.a);
	}

	public static class A {
		public String a = "aaaa";
	}

}
