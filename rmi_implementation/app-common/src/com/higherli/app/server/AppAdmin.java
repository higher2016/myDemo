package com.higherli.app.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import org.apache.commons.lang3.StringUtils;

import com.higherli.app.RmiHelper;
import com.higherli.app.common.server.ICRemote;
import com.higherli.app.common.server.MethodSign;
import com.higherli.app.dto.ISystem;

public class AppAdmin {
	public static final String ShutDown = "shutdown";
	public static final String Command = "cmd";

	public static void main(String[] args) {
		if (args.length == 0) {
			InputStreamReader reader = new InputStreamReader(System.in);
			args = askForLine(reader, "").split(" ");
		}
		String[] cmds = new String[args.length - 1];
		for (int index = 1; index < args.length; index++) {
			cmds[(index - 1)] = args[index];
		}
		if ("shutdown".equalsIgnoreCase(args[0])) {
			int port = Integer.parseInt(cmds[0]);
			shutdownServer(port);
		} else if ("cmd".equalsIgnoreCase(args[0])) {
			String serviceName = args[0];
			String ip = args[1];
			int port = Integer.parseInt(args[2]);
			String cmd = args[3];
			Object[] params = new Object[args.length - 4];
			System.arraycopy(args, 4, params, 0, args.length - 4);
			command(serviceName, ip, port, cmd, params);
		}
	}

	/**
	 * 通过映射的方式执行远程对象中的方法
	 */
	public static void command(String serviceName, String ip, int port, String cmd, Object[] params) {
		try {
			ICRemote appRemote = null;
			if (ip.equalsIgnoreCase("localhost"))
				appRemote = (ICRemote) RmiHelper.getRemote(serviceName, port);
			else
				appRemote = (ICRemote) RmiHelper.getRemote(serviceName, ip, port);
			System.out.println(String.format("<Command> service[%s:%s:%s], command: %s(%s)",
					new Object[] { ip, Integer.valueOf(port), serviceName, cmd, StringUtils.join(params, ", ") }));
			 MethodSign methodSign = new MethodSign(cmd, new Class[] { Object[].class });
			String msg = (String) appRemote.exe(methodSign, new Object[] { params });
			System.out.println("\t" + msg);
		} catch (Exception e) {

		}
	}

	public static void shutdownServer(int port) {
		String name = "_system";
		System.out.println(String.format("<Shutdown Server>! port[%s]", new Object[] { Integer.valueOf(port) }));
		ISystem system = RmiHelper.getRemote(name, port);
		try {
			System.out.println(system.stopserver());
		} catch (RemoteException e) {
			System.err.println("Shutdown Server error!");
			e.printStackTrace();
		}
	}

	private static String askForLine(InputStreamReader reader, String prompt) {
		String result = "";
		while ((result != null) && (result.length() == 0)) {
			System.out.print(prompt);
			System.out.flush();
			result = getLine(reader);
		}
		return result;
	}

	private static String getLine(InputStreamReader reader) {
		StringBuilder b = new StringBuilder();
		int c = 0;
		try {
			do {
				if (c != 13)
					b.append((char) c);
				if ((c = reader.read()) == -1)
					break;
			} while (c != 10);
		} catch (IOException ioe) {
			c = -1;
		}

		if ((c == -1) && (b.length() == 0)) {
			return null;
		}
		return b.toString();
	}
}
