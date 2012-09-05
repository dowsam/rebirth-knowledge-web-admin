/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin MonitorServiceProvider.java 2012-7-24 13:18:22 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.monitor;

import java.io.File;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.rebirth.commons.settings.ImmutableSettings;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.monitor.MonitorService;
import cn.com.rebirth.core.monitor.dump.DumpMonitorService;
import cn.com.rebirth.core.monitor.fs.FsProbe;
import cn.com.rebirth.core.monitor.fs.FsService;
import cn.com.rebirth.core.monitor.fs.SigarFsProbe;
import cn.com.rebirth.core.monitor.jvm.JvmMonitorService;
import cn.com.rebirth.core.monitor.jvm.JvmService;
import cn.com.rebirth.core.monitor.network.NetworkProbe;
import cn.com.rebirth.core.monitor.network.NetworkService;
import cn.com.rebirth.core.monitor.network.SigarNetworkProbe;
import cn.com.rebirth.core.monitor.os.OsProbe;
import cn.com.rebirth.core.monitor.os.OsService;
import cn.com.rebirth.core.monitor.os.SigarOsProbe;
import cn.com.rebirth.core.monitor.process.ProcessProbe;
import cn.com.rebirth.core.monitor.process.ProcessService;
import cn.com.rebirth.core.monitor.process.SigarProcessProbe;
import cn.com.rebirth.core.monitor.sigar.SigarService;
import cn.com.rebirth.core.threadpool.ThreadPool;

/**
 * The Class MonitorServiceProvider.
 *
 * @author l.xue.nong
 */
public final class MonitorServiceProvider implements InitializingBean, DisposableBean {

	/**
	 * The Class MonitorServiceProviderConfig.
	 *
	 * @author l.xue.nong
	 */
	@Configuration
	public static class MonitorServiceProviderConfig {

		/**
		 * Gets the monitor service provider.
		 *
		 * @return the monitor service provider
		 */
		@Bean
		public MonitorServiceProvider getMonitorServiceProvider() {
			return MonitorServiceProvider.getInstance();
		}
	}

	/** The monitor service. */
	private MonitorService monitorService;

	/**
	 * Instantiates a new monitor service provider.
	 */
	private MonitorServiceProvider() {
		super();
		if (monitorService == null) {
			Settings settings = ImmutableSettings.Builder.EMPTY_SETTINGS;
			ThreadPool threadPool = new ThreadPool();
			DumpMonitorService dumpMonitorService = new DumpMonitorService();
			JvmMonitorService jvmMonitorService = new JvmMonitorService(settings, threadPool, dumpMonitorService);
			SigarService sigarService = new SigarService(settings);
			OsProbe osProbe = new SigarOsProbe(settings, sigarService);
			OsService osService = new OsService(settings, osProbe);
			ProcessProbe probe = new SigarProcessProbe(settings, sigarService);
			ProcessService processService = new ProcessService(settings, probe);
			JvmService jvmService = new JvmService(settings);
			NetworkProbe networkProbe = new SigarNetworkProbe(settings, sigarService);
			NetworkService networkService = new NetworkService(settings, networkProbe);
			File[] dataLocations = new File[] { new File(System.getProperty("user.dir")) };
			FsProbe fsProbe = new SigarFsProbe(settings, sigarService, dataLocations, true);
			FsService fsService = new FsService(settings, fsProbe);
			this.monitorService = new MonitorService(settings, jvmMonitorService, osService, processService,
					jvmService, networkService, fsService);
		}
	}

	/**
	 * Gets the single instance of MonitorServiceProvider.
	 *
	 * @return single instance of MonitorServiceProvider
	 */
	public static MonitorServiceProvider getInstance() {
		return InstanceHadle.monitorServiceProvider;
	}

	/**
	 * The Class InstanceHadle.
	 *
	 * @author l.xue.nong
	 */
	private static class InstanceHadle {

		/** The monitor service provider. */
		static MonitorServiceProvider monitorServiceProvider = new MonitorServiceProvider();
	}

	/**
	 * Gets the monitor service.
	 *
	 * @return the monitor service
	 */
	public MonitorService getMonitorService() {
		return monitorService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		monitorService.stop();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.monitorService.start();
	}

}
