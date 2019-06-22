<%@ page import="com.mathworks.toolbox.javabuilder.*"%>
<%@ page
	import="com.mathworks.toolbox.javabuilder.webfigures.components.MathWorksLogo"%>
<%@ page import="com.mathworks.toolbox.javabuilder.MWJavaObjectRef"%>
<%@ page import="com.mathworks.toolbox.javabuilder.webfigures.WebFigure"%>
<%@ taglib prefix="wf"
	uri="http://www.mathworks.com/builderja/webfigures.tld"%>
<%@ page import="getPlot.Class1"%>
<HTML>
<BODY>
	<%!Class1 myDeployedComponent;%>
	<%!public void jspInit() {
		try {
			//Instantiate the Deployed Component
			myDeployedComponent = new Class1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}%>
	<%!public void jspDestroy() {
		if (myDeployedComponent != null) {
			myDeployedComponent.dispose();
		}
		myDeployedComponent = null;
	}%>
	<%
		if (myDeployedComponent != null) {
			try {
				// Get the WebFigure from your function's output
				WebFigure webFigure = (WebFigure) ((MWJavaObjectRef) myDeployedComponent.getPlot(1)[0]).get();
				// set it to the tag
				request.getSession().setAttribute("MyFigure", webFigure);
			} catch (ClassCastException e) {
				throw new Exception("Issue casting deployed components outputs to WebFigure", e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			out.println("no go");
		}
	%>
	<wf:web-figure name="MyFigure" scope="session" />
</BODY>
</HTML>