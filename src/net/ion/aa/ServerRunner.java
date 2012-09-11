package net.ion.aa;

import java.io.File;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationFactory;
import net.ion.framework.template.InvalidSetupException;
import net.ion.framework.util.Debug;
import net.ion.framework.util.InfinityThread;
import net.ion.radon.Options;
import net.ion.radon.core.AradonServer;

public class ServerRunner {

	public static void main(String[] args) throws Exception {

		String configFileName = args[0];
		
		if(isNotExist(configFileName)){
			configFileName = "./conf/"+ configFileName;
			if(isNotExist(configFileName)){
				configFileName = "."+ configFileName;
			}
		}

		AradonServer as = new AradonServer(makeOptions(configFileName));
		as.start();	
		
		new InfinityThread().startNJoin();
	}
	
	private static boolean isNotExist(String file){
		return !(new File(file).canRead());
	}
	

	public static Options makeOptions(String configFileName) throws InvalidSetupException {
		try {

			Configuration config;
			ConfigurationFactory defaultConfigFactory = ConfigurationFactory.getInstance(configFileName);
			defaultConfigFactory.build(configFileName);
			config = defaultConfigFactory.getConfiguration("root");
			
			Options opt = new Options(new String[] {"-port:"+config.getAttribute("port"), "-config:"+configFileName});
			
			Debug.debug(opt.getInt("port", 11));
			Debug.debug(opt.getString("config", "null"));
			
			
			return opt;
			
		}  catch (Throwable ex) {
			Debug.debug(ex.getMessage());
			throw new InvalidSetupException(ex.getMessage(), ex);
		}
	}
	
}
