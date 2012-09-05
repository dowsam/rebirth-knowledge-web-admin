/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin OnlineSysUserSerializable.java 2012-9-4 9:51:41 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.AbstractContainer;
import cn.com.rebirth.commons.Container;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;

/**
 * The Class OnlineSysUserSerializable.
 *
 * @author l.xue.nong
 */
public class OnlineSysUserSerializable extends AbstractContainer implements Container {
	
	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/** The persistence. */
	protected boolean persistence = true;
	/** The persistence path. */
	protected String persistencePath = System.getProperty("java.io.tmpdir") + File.separator + "onlineSysUser";

	/** The persistence lock. */
	protected Object persistenceLock = new Object();

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Container#start()
	 */
	@Override
	public void start() {
		if (persistence) {
			synchronized (persistenceLock) {
				try {
					restore();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Gets the persistence file name.
	 *
	 * @return the persistence file name
	 */
	protected String getPersistenceFileName() {
		return OnlineSysUserSerializable.class.getSimpleName();
	}

	/**
	 * Gets the persistence dir.
	 *
	 * @return the persistence dir
	 */
	protected File getPersistenceDir() {
		File parentDir = new File(persistencePath + File.separator);
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		return parentDir;
	}

	/**
	 * Restore.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void restore() throws IOException {
		ObjectInputStream ois = null;
		File file = new File(getPersistenceDir(), getPersistenceFileName());

		if (file.exists()) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				int count = 0;
				while (true) {
					try {
						OnlineSysUserEntity message = (OnlineSysUserEntity) ois.readObject();
						UserService.put(message.getSessionId(), message);
						if (message.isFail()) {
							UserService.putFail(message.getSessionId());
						}
						count++;
					} catch (EOFException e) {
						break;
					} catch (Exception e) {
						continue;
					}
				}
				logger.info("{}已从{}中恢复{}个序列化.",
						new Object[] { getPersistenceFileName(), file.getAbsolutePath(), count });
			} finally {
				if (ois != null) {
					ois.close();
				}
			}
			file.delete();
		} else {
			logger.debug("{}的持久化文件{}不存在", getPersistenceFileName(), file.getAbsolutePath());
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Container#stop()
	 */
	@Override
	public void stop() {
		if (persistence) {
			synchronized (persistenceLock) {
				try {
					backup();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Backup.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void backup() throws IOException {
		Collection<OnlineSysUserEntity> list = UserService.all();
		if (!list.isEmpty()) {
			ObjectOutputStream oos = null;
			try {
				File file = new File(getPersistenceDir(), getPersistenceFileName());
				oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				for (Object message : list) {
					try {
						oos.writeObject(message);
					} catch (Exception e) {
						continue;
					}
				}
				logger.info("{}已持久化{}个序列化到{}",
						new Object[] { getPersistenceFileName(), list.size(), file.getAbsolutePath() });
			} finally {
				if (oos != null) {
					oos.close();
				}
			}
		} else {
			logger.debug("队列{}为空,不需要持久化 .", getPersistenceFileName());
		}
	}

	/**
	 * Checks if is persistence.
	 *
	 * @return true, if is persistence
	 */
	public boolean isPersistence() {
		return persistence;
	}

	/**
	 * Sets the persistence.
	 *
	 * @param persistence the new persistence
	 */
	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	/**
	 * Gets the persistence path.
	 *
	 * @return the persistence path
	 */
	public String getPersistencePath() {
		return persistencePath;
	}

	/**
	 * Sets the persistence path.
	 *
	 * @param persistencePath the new persistence path
	 */
	public void setPersistencePath(String persistencePath) {
		this.persistencePath = persistencePath;
	}

}
