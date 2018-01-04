import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

class product {
    // Fields of a product available for purchase
    String name;
    String imgPath;
    double price;

    product(String s, String i, double p){
	name=s; imgPath=i; price=p;
    }
}

class Customer {
    // Fields that each user has
    String Password;
    String Full_Name;
    String Address;
    //long sessionTime;
    ArrayList<Integer> cart;
    ArrayList<Integer> numToBuy;

    Customer() {
	cart = new ArrayList<Integer>();
	numToBuy = new ArrayList<Integer>();
    }

    public String toString(){
	return "Password="+Password+", Full Name="+Full_Name+", Address="+Address;
    }
}


public class signupForm extends HttpServlet {

    static final boolean debug = false;

    // Stores user info by username
    private Hashtable<String,Customer> userTable = new Hashtable<String,Customer>();
    private String currentUser=null;

    // Array of products. Product IDs map to this array
    product[] Products;

    public signupForm() {
	super();
	Products  = new product[5];
	Products[0] = new product("Distilled Water",
				  "/a4html/pictures/distilled.jpg",5.99);
	Products[1] = new product("Spring Water",
				  "/a4html/pictures/spring.jpg",8.99);
	Products[2] = new product("Pretentious Water",
				  "/a4html/pictures/fancy.jpg",39.99);
	Products[3] = new product("Foreign Water",
				  "/a4html/pictures/foreign.jpg",19.99);
	Products[4] = new product("Bubbly Water",
				  "/a4html/pictures/bubbly.jpg",12.99);
    }

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
    {
	// Set the content type of the response.
	resp.setContentType ("text/html");
    
	// Extract the PrintWriter to write the response.
	PrintWriter out = resp.getWriter ();
    
	// The first part of the response.
	out.println ("<html>");
	out.println ("<head><title> Account Creation </title></head>");
	out.println ("<body style=\"background-color: #cce6ff; font-family: 'Courier New',monospace;\">");

	// Print parameters to aid debugging
	if(debug)
	    printParams(out, req);

	String pageName = req.getParameter("page");

	if(pageName.equalsIgnoreCase("signup"))
	    handleSignUp(out, req);
	else if(pageName.equalsIgnoreCase("logout"))
	    handleLogout(out);
	else if(pageName.equalsIgnoreCase("addCart"))
	    addToCart(out, req);
	else if(pageName.equalsIgnoreCase("cart"))
	    checkout(out);
	else if(pageName.equalsIgnoreCase("account"))
	    account(out);
	else if(pageName.equalsIgnoreCase("purchase"))
	    handlePurchase(out);
	else if(pageName.equalsIgnoreCase("clear"))
	    clearCart(out);

    
	// Last part.
	out.println ("</body>");
	out.println ("</html>");
	out.flush();
    
	// Screen I/O
	System.out.println ("Inside servlet ... servlet complete");
    }
  
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
    {
	doGet (req, resp);
    }

    void handleSignUp(PrintWriter out, HttpServletRequest req) 
    {
	out.println("<hr>");
	
	if(currentUser == null){
	    // Put user data in the Hashtable
	    Customer c = new Customer();
	    c.Password = req.getParameter("password");
	    c.Full_Name = req.getParameter("fullname");
	    c. Address = req.getParameter("address");
	    currentUser = req.getParameter("username");
	    userTable.put(currentUser, c);
	
	    out.println("<h4>"+currentUser+", your account has been sucessfully created </h4>");
	}
	else {
	    out.println("<h4>"+currentUser+", you are already logged in</h4>");
	}

	    out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><br>");

	    // Form to take you to account page
	    out.println("<form action=\"/servlets/a4servlets/signupForm\" method=\"post\">");
	    out.println("<button type=\"submit\" name=\"accountBtn\">");
	    out.println("See Your Account</button>");
	    out.println("<input name=\"page\" value=\"account\" type=\"hidden\">");
	    out.println("</form>");

	    // Form to take you to logout page
	    out.println("<form action=\"/servlets/a4servlets/signupForm\" method=\"post\">");
	    out.println("<button type=\"submit\" name=\"logoutBtn\">Logout</button>");
	    out.println("<input name=\"page\" value=\"logout\" type=\"hidden\">");
	    out.println("</form>");


	if(debug){
	    Customer c = (Customer) userTable.get(currentUser);
	    out.println("<ul><li> table entries: "+ c);	
	}
    }


    // Logs a user out
    void handleLogout(PrintWriter out)
    {
	if(currentUser != null){
	    currentUser = null;
	    out.println("<h4>You have been successfully logged out</h4>");
	}
	else{
	    out.println("<h4>You can't logout if you aren't logged in</h4>");
	}
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a>");

    }


    // Add an Item to your cart
    void addToCart(PrintWriter out, HttpServletRequest req)
    {
	out.println("<hr>");

	// Can only add to cart if logged in
	if(currentUser == null){
	    out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><br>");
	    out.println("<h4>You need to be logged in to add items to your cart<h4>");

	    // Link to signup page
	    out.println("<p>Make an Account: ");
	    out.println("<a href=\"/a4html/login.html\">Here</a>");
	    return;
	}

	// Add products and amount to respective lists
	Customer c = (Customer) userTable.get(currentUser);
	int productIndex = Integer.parseInt(req.getParameter("productIndex"));
	int quantity = Integer.parseInt(req.getParameter("quantity"));
	c.cart.add(productIndex);
	c.numToBuy.add(quantity);

	out.println("<h4>Successfully added "+quantity+" items of "+Products[productIndex].name+"<h4>");
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><br>");
    }

    void account(PrintWriter out)
    {
	// Need to be logged in
	if(currentUser == null){
	    out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><br>");
	    out.println("<h4>You need to be logged in to see your account info<h4>");

	    // Link to signup page
	    out.println("<p>Make an Account: ");
	    out.println("<a href=\"/a4html/login.html\">Here</a>");
	    return;
	}

	Customer c = (Customer) userTable.get(currentUser);
	out.println("<hr><h1>Your Account info</h1>");
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><hr>");

	out.println("<p><b>Username:</b> "+currentUser+"<br>");
	out.println("<p><b>Password:</b> "+c.Password+"<br>");
	out.println("<p><b>FullName:</b> "+c.Full_Name+"<br>");
	out.println("<p><b>Address:</b> "+c.Address+"<br>");
    }

    void checkout(PrintWriter out)
    {
	// CSS to make table look nice
	out.println("<style>th, td {border-bottom: 2px solid #99003d;}");
	out.println("tr:hover {background-color: #fff;}</style>");

	// Can only see cart if logged in
	if(currentUser == null){
	    out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><br>");
	    out.println("<h4>You need to be logged in to add items to your cart<h4>");

	    // Link to signup page
	    out.println("<p>Make an Account: ");
	    out.println("<a href=\"/a4html/login.html\">Here</a>");
	    return;
	}

	// Add products and amount to respective lists
	Customer c = (Customer) userTable.get(currentUser);
	ArrayList<Integer> cart = c.cart;
	ArrayList<Integer> numToBuy = c.numToBuy;
	double bill = 0;

	out.println("<hr><h1>Your Cart</h1>");
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><hr>");

	out.println("<table><thead><tr>");
	out.println("<th>Name</th><th colspan=\"1\">Image</th>");
	out.println("<th>Price</th><th colspan=\"1\">Amount</th></tr></thead><tbody>");

	for(int i=0; i<cart.size(); i++)
	{
	    out.println("<tr><td>"+Products[cart.get(i)].name+"</td>");
	    out.println("<td><img src="+Products[cart.get(i)].imgPath+" height=\"100px\" width=\"100px\"></td>");
	    out.println("<td>$"+Products[cart.get(i)].price+"</td>");
	    out.println("<td>"+numToBuy.get(i)+"</td></tr>");

	    bill = bill +(Products[cart.get(i)].price)*numToBuy.get(i);
	}
	out.printf("<p>Your total is: $%.2f",bill);
	
	// Form to confirm checkout
	out.println("<form action=\"/servlets/a4servlets/signupForm\" method=\"post\">");
	out.println("<button type=\"submit\" name=\"buyBtn\">Purchase</button>");
	out.println("<input name=\"page\" value=\"purchase\" type=\"hidden\">");
	out.println("</form>");

	// Form to clear cart
	out.println("<form action=\"/servlets/a4servlets/signupForm\" method=\"post\">");
	out.println("<button type=\"submit\" name=\"clearBtn\">Clear Cart</button>");
	out.println("<input name=\"page\" value=\"clear\" type=\"hidden\">");
	out.println("</form>");
    }

    void handlePurchase(PrintWriter out)
    {
	Customer c = (Customer) userTable.get(currentUser);
	c.cart.clear();
	c.numToBuy.clear();

	out.println("<hr><h2>You have successfully purchased your water</h2>");
	out.println("<h4>Stay Thristy My Friends</h4>");
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><hr>");

    }


    void clearCart(PrintWriter out)
    {
	// Clear ArrayLists for the customer
	Customer c = (Customer) userTable.get(currentUser);
	c.cart.clear();
	c.numToBuy.clear();

	out.println("<hr><h2>Your cart has been cleared</h2>");
	out.println("<b><a href=\"/a4html/home.html\">Return Home</a></b><hr>");
    }

    void printParams(PrintWriter out, HttpServletRequest req)
    {
	// Now get the parameters and output them back.
	out.println ("<p><hr><p>Request parameters: <ul>");
	Enumeration e = req.getParameterNames();
	while (e.hasMoreElements()) {
	    String name = (String) e.nextElement();
	    String value = req.getParameter (name);
	    if (value != null)
		out.println("<li> name=[" + name + "] value=[" + value + "]");
	    else
		out.println("<li> name=[" + name + "] did not have a value");
	}
	out.println("<hr>");
    }
  
}
