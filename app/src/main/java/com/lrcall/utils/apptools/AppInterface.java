package com.lrcall.utils.apptools;

import com.lrcall.utils.ShellUtils;

import java.io.File;
import java.util.List;

/**
 * Created by libit on 15/9/29.
 */
public interface AppInterface
{
	/**
	 * 启用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	ShellUtils.CommandResult enableApp(String packageName);

	/**
	 * 启用Apps
	 *
	 * @param packageNames App包名列表
	 * @return
	 */
	ShellUtils.CommandResult enableApps(List<String> packageNames);

	/**
	 * 禁用App
	 *
	 * @param packageName App包名
	 * @return
	 */
	ShellUtils.CommandResult disableApp(String packageName);

	/**
	 * 禁用Apps
	 *
	 * @param packageNames App包名列表
	 * @return
	 */
	ShellUtils.CommandResult disableApps(List<String> packageNames);

	/**
	 * 启动应用程序
	 *
	 * @param packageName 应用包名
	 * @return 启动成功返回true，失败返回false
	 */
	boolean startApp(String packageName);

	/**
	 * 关闭正在运行的程序信息
	 *
	 * @param packageName
	 * @return
	 */
	ShellUtils.CommandResult killApp(String packageName);

	/**
	 * 安装App
	 *
	 * @param file 安装包
	 */
	void installApp(File file);

	/**
	 * 卸载App
	 *
	 * @param packageName App包名
	 */
	void uninstallApp(String packageName);
}
