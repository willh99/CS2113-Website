import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class VisitorCount extends HttpServlet {

    static final boolean debug = false;

    int numLogins = 0;

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
    {
	// Set the content type of the response etc ..
	resp.setContentType ("text/html");
	PrintWriter out = resp.getWriter ();
	out.println ("<html><head><title> Visit Count</title></head><body>");

	// During debugging, it helps to print out the parameters in the response.
	if (debug) {
	    printParams (out, req);
	}

	// Write out the action.
	// CHANGE TO YOUR PORT NUMBER:
	out.println ("<form action=\"http://unix.seas.gwu.edu:40013/servlets/examples/VisitorCount\" method=\"post\">");

	// out.println ("<form action=\"http://localhost:40013/servlets/examples/VisitorCount\" method=\"post\">");


	// Get the page info from hidden fields:
	String whichPage = req.getParameter ("page");
	String userName = req.getParameter ("username");

	// Determine action based on which page fired the request.
	if (whichPage.equalsIgnoreCase ("loginpage")) {
	    handleLoginPage (out, userName);
	}
	else if (whichPage.equalsIgnoreCase ("menupage")) {
	    handleMenuPage (out, userName, req);
	}
	else if (whichPage.equalsIgnoreCase ("featurepage")) {
	    // Back button on Time or Fortune page
	    handleLoginPage (out, userName);
	}
	else {
	    handleError (out);
	}
    
	// Last part.
	out.println ("</form></body></html>");
	out.flush ();
    }
  

    public void doPost (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
    {
	// We need to implement POST because the forms are written as such.
	doGet (req, resp);
    }
  

    void handleLoginPage (PrintWriter out, String userName)
    {

	// Login count.

	// Deliberately slow it down for users that start with "M".
	if (userName.startsWith ("M")) {
	    try {
		Thread.sleep (5000);
	    }
	    catch (InterruptedException e) {
		// Handle exception ...
	    }
	}

	numLogins ++;
	out.println ("Number of people who logged in before you: " + numLogins + "<p>");
	out.println ("Hello " + userName + "! Choose from these options: ");

	// The two buttons.
	out.println ("<input type=\"submit\" name=\"Time\" value=\"Time\">");
	out.println ("<input type=\"submit\" name=\"Fortune\" value=\"Fortune\">");

	// Write out the extracted username:
	out.println ("<input type=\"hidden\" name=\"username\" value=\"" + userName + "\">");
	out.println ("<input type=\"hidden\" name=\"page\" value=\"menupage\">");
    }


    void handleMenuPage (PrintWriter out, String userName, HttpServletRequest req)
    {
	// Check which button was pressed:
	String timeButtonPressed = req.getParameter ("Time");

	out.println (userName + ", ");

	if (timeButtonPressed != null) {
	    // Time event.
	    Date d = new Date ();
	    out.println ("the date/time is: " + d + ".");
	}
	else {
	    // Fortune event.
	    String fortune = "";
	    if ( (int)userName.charAt(0) % 2 == 0) {
		fortune = "go out, meet people. Tomorrow is going to be great for you.";
	    }
	    else {
		fortune = "stay at home. Tomorrow is going to be a lousy day for you.";
	    }
	    out.println (fortune);
	}

	// A "Back" button:
	out.println ("<br><input type=\"submit\" name=\"Back\" value=\"Back\">");

	// Need to write the hidden fields:
	out.println ("<input type=\"hidden\" name=\"username\" value=\"" + userName + "\">");
	out.println ("<input type=\"hidden\" name=\"page\" value=\"featurepage\">");
    }


    void handleError (PrintWriter out)
    {
	out.println ("Enter username: <input type=\"text\" name=\"username\">");
	out.println ("<input type=\"submit\" value=\"login\">");
	out.println ("<input type=\"hidden\" name=\"page\" value=\"loginpage\">");
    }


    // For debugging:

    void printParams (PrintWriter out, HttpServletRequest req)
    {
	out.println ("<p><hr><p> Request parameters: <ul>");
	Enumeration e = req.getParameterNames();
	while (e.hasMoreElements()) {
	    String name = (String) e.nextElement();
	    String value = req.getParameter (name);
	    if (value != null)
		out.println ("<li> name=[" + name + "] value=[" + value + "]");
	    else
		out.println ("<li> name=[" + name + "] did not have a value");
	}
	out.println ("</ul><hr>");
    }

}
