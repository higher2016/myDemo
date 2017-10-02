package com.higherli.app;

/**
 * 远程对象信息（host:服务器ip;port:服务器端口号;name）
 * 
 * Simple class to enable multiple URL return values.
 * 
 * 参考 java.rmi.Naming.ParsedNamingURL类
 * 
 */
public class RemoteNode implements java.io.Serializable {

	private static final long serialVersionUID = -3450090401607540083L;
	public volatile String host;
	public volatile int port;
	public volatile String name;
	private int prime = 31;
	private int result = 1;

	public RemoteNode(String host, int port, String name) {
		super();
		this.host = host;
		this.port = port;
		this.name = name;
	}

	@Override
	public int hashCode() {
		result = prime * result + (this.host == null ? 0 : this.host.hashCode());
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		result = prime * result + this.port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RemoteNode))
			return false;
		RemoteNode other = (RemoteNode) obj;
		if (this.host == null) {
			if (other.host != null)
				return false;
		} else if (!this.host.equals(other.host)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return this.port == other.port;
	}

	@Override
	public String toString() {
		return String.format("host[%s] port[%s] name[%s]",
				new Object[] { this.host, Integer.valueOf(this.port), this.name });
	}

}
