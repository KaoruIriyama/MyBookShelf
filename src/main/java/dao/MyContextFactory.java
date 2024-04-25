package dao;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

//https://blog.sgnet.co.jp/2017/12/java-datasource.html
public class MyContextFactory implements InitialContextFactory {
	private static final Context CONTEXT = new MyContext();
	@Override
	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		return CONTEXT;
	}

}
