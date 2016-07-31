<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 

<title>Editer les sous-titres</title>

    <meta name="description" content="Editer les sous-titres">

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="css/general.css" />
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->


</head>
<body>
    
    <div class="container-fluide">
	 <nav class="navbar navbar-inverse navbar-fixed-top">
      
    <form class="navbar-form">
    fichier source: 
      <input type="text" name="FileNameSource" id="FileNameSource" value="${ FileNameSource }" size="35px" />
        <c:if test="${ not empty FileNameDestination }">
       		<a href="http://localhost:8080/Subtitlor${ FileNameDestination }" >
    		    <button>fichier</button> 
     		</a>
        </c:if>
        <input type="submit" value="enregistrer" />
    </form>
	</nav>
  

 	    <table>
	        <c:forEach items="${ subtitles }" var="line" >
	        	<tr>
	        		<td style="text-align:right;"><c:out value="${ line.numLigne }" />     
	        		<td/>
	        		 <td>
	        		  <c:out value="${line.time }" />
	        		  </td>
	        		  </tr>
	        		      <c:forEach items="${ line.original }" var="line1" varStatus="status">
	        		       <tr>
	        		  		<td/>
	        		        <td>
	        		        <c:out value="${ line1 }" />
	        				</td>
	        				<td><input type="text" name="paragraphe${ line.numLigne }linge${ status.index }" id="paragraphe${ line.numLigne }linge${ status.index }" value="${ line.traduit[status.index] }" size="35" /></td>
	        				</tr>
	        				</c:forEach>
	        				<tr>
	        				 <td/>
	        				 </tr>	        	
	    	</c:forEach>
	    </table>
    </div>
</body>
</html>