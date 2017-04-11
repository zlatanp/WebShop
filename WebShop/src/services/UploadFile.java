package services;



import javax.ws.rs.Path;



@Path("/file")
public class UploadFile {
/*
	@Context
	ServletConfig config;
	
		@POST
		@Path("/upload")
		@Consumes(MediaType.MULTIPART_FORM_DATA)
		public Response uploadImages(@FormDataParam("file") InputStream in,@FormDataParam("file") FormDataContentDisposition info,@PathParam("productName") String name) throws IOException{
			
			if("".equals(name)){
				URI target = null;
				try {
					target = new URI("/WebShop/MainPage.html");
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Response.temporaryRedirect(target).build();
			}
			
			
			String s = config.getServletContext().getRealPath("");
			String path = FilePaths.getMediaPath(s).getPath();
			
			try{
				int read = 0;
				File file = new File(path+"\\"+name+"Image"+info.getFileName());
				if(file.exists()){
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}
				byte[] bytes = new byte[1024];
				OutputStream out = new FileOutputStream(file);
				while((read=in.read(bytes))!= -1){
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			}catch(IOException e){
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			
			return Response.ok().build();
		}
	*/
}
