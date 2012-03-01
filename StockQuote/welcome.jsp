<HTML> 
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>RealTime Stock Portfolio</title>
<link href="modern-table-template/style.css" rel="stylesheet" type="text/css" />
</HEAD>  
<BODY>

<div align="center" id="psdgraphics-com-table">


<div id="psdg-header">
<span class="psdg-bold">RealTime Stock Portfolio</span><br />

<%
   if(session.getAttribute("username")!=null && session.getAttribute("username")!="")
   {
         String user = session.getAttribute("username").toString();
%>
Welcome, <%= user%>
<!--<TR><TD align="center"><H1>
Welcome <B><%= user%></B></H1></TD></TR> -->
<%
   }
%>
</div>


<div id="psdg-top">
<div class="psdg-top-cell" style="width:129px; text-align:left; padding-left: 24px;">Stock Symbol</div>
<div class="psdg-top-cell">Price</div>
<div class="psdg-top-cell">Date</div>
<div class="psdg-top-cell">Time</div>
<div class="psdg-top-cell">Change</div>
<div class="psdg-top-cell">Open</div>
<div class="psdg-top-cell">Days Range Hi</div>
<div class="psdg-top-cell">Days Range lo</div>
<div class="psdg-top-cell" style="border:none;">Volume</div>
</div>

<%@ page import="java.net.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.StringTokenizer"%>

<%!
static String stripLeadingAndTrailingQuotes(String str)
{
      if (str.startsWith("\""))
      {
          str = str.substring(1, str.length());
      }
      if (str.endsWith("\""))
      {
          str = str.substring(0, str.length() - 1);
      }
      return str;
}
%>

<%
String symbol = null;
String price = null;
String date = null;
String time = null;
String change = null;
String openPrice = null;
String highRange = null;
String lowRange = null;
String volume = null;

if(session.getAttribute("symbol1")!=null && session.getAttribute("symbol1")!="" && session.getAttribute("symbol2")!=null && session.getAttribute("symbol2")!="" && session.getAttribute("symbol3")!=null && session.getAttribute("symbol3")!="")
{
   String sym1 = session.getAttribute("symbol1").toString();
   String sym2 = session.getAttribute("symbol2").toString();
   String sym3 = session.getAttribute("symbol3").toString();

   URL yahooUrl = new URL("http://finance.yahoo.com/d/quotes.csv?s="+sym1+","+sym2+","+sym3+"&format=sl");

   String inputLine;
   BufferedReader urlStrm = new BufferedReader(new InputStreamReader(yahooUrl.openStream()));
%>

<%
while ((inputLine = urlStrm.readLine()) != null) {
%>

<%
    StringTokenizer st = new StringTokenizer(inputLine, ",");
    while(st.hasMoreTokens()){
       symbol = st.nextToken();
       price = st.nextToken();
       date = st.nextToken();
       time = st.nextToken();
       change = st.nextToken();
       openPrice = st.nextToken();
       highRange = st.nextToken();
       lowRange = st.nextToken();
       volume = st.nextToken();
%>
<!--<P><%= stripLeadingAndTrailingQuotes(symbol)%></P>
<P><%= price%></P>
<P><%= stripLeadingAndTrailingQuotes(date)%></P>
<P><%= stripLeadingAndTrailingQuotes(time)%></P>
<P><%= change%></P>
<P><%= openPrice%></P>
<P><%= highRange%></P>
<P><%= lowRange%></P>
<P><%= volume%></P>-->
<div id="psdg-middle">

<div class="psdg-left"><b><%= stripLeadingAndTrailingQuotes(symbol)%></b></div>
<div class="psdg-right"><b><%= price%></b></div>
<div class="psdg-right"><b><%= stripLeadingAndTrailingQuotes(date)%><b></div>
<div class="psdg-right"><b><%= stripLeadingAndTrailingQuotes(time)%></b></div>
<div class="psdg-right"><b><%= change%></b></div>
<div class="psdg-right"><b><%= openPrice%></b></div>
<div class="psdg-right"><b><%= highRange%></b></div>
<div class="psdg-right"><b><%= lowRange%></b></div>
<div class="psdg-right"><b><%= volume%></b></div>


</div>

<%  
   }
  }

urlStrm.close();
}
%>

<div id="psdg-footer">
&nbsp;
</div>





</div>


</BODY>
<HTML>
