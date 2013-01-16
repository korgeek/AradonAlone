package net.ion.aa.let;

import java.io.File;

import net.ion.framework.util.PathMaker;
import net.ion.framework.util.StringUtil;
import net.ion.radon.core.let.AbstractServerResource;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public class DirExtraLet extends AbstractServerResource{


	@Get
	public Representation getFile() throws Exception {
		String filePath = PathMaker.getFilePath(getContext().getAttributeObject("base.dir", "./", String.class), getRequest().getResourceRef().getPath()) ;
		int timeToLive = getContext().getAttributeObject("time.to.live", 585858, Integer.class);
		File file = new File(filePath) ;

		if (! file.exists()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, getRequest().getResourceRef().getPath()) ; 

		MediaType mtype = getMetadataService().getMediaType(StringUtil.substringAfterLast(file.getName(), ".")) ;
		if (mtype == null) mtype = MediaType.ALL ; 
		
		final FileRepresentation result = new FileRepresentation(file, mtype, timeToLive);
		return result;
	}

	@Post
	public Representation getFileWithPost(Representation entity) throws Exception {
		return getFile();
	}
}
