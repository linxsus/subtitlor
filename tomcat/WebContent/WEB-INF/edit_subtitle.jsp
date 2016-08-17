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
 <div class="center container row">
    <form  method="post" enctype="multipart/form-data" >
   
	 <nav class="navbar navbar-inverse navbar-fixed-top navbar-form">
	  <div class=" form-group form-inline">  
	  <div class="row">
       <input class="btn btn-info col-lg-6" type="file" accept=".srt"  name="FileNameSource" />
       <div class="col-lg-6"> 
        <input class="btn btn-info" type="submit" name="charger" value="charger" id="charger" />
        <c:if test="${ not empty FileNameDestination }">
       		<a class="btn btn-info" href="Srt/${ FileNameDestination }" >
    		    fichier
     		</a>
        </c:if>
        <input class="btn btn-info" type="submit" name="enregistrer" value="enregistrer" />
        </div>
        </div>
        <div class="row">
         <ul class="pagination pagination-sm">
          <c:if test="${ not empty chevronGauche }">
             <li><a href="edit?pagination=${debutPagi-1}">&laquo;</a></li>
          </c:if>
          <c:forEach var="i" begin="${ debutPagi }" end="${ finPagi }">
            <li 
             <c:if test="${ i eq pagination }">
              class="active"
             </c:if>
             >
             <a href="edit?pagination=${i}"> ${i} </a></li>
          </c:forEach>
          
          <c:if test="${ not empty chevronDroit }">
             <li><a href="edit?pagination=${finPagi+1}">&raquo;</a></li>
          </c:if>
          
         </ul>
        </div>
      </div>
	</nav>
  

	        <c:forEach items="${ subtitles }" var="line" >
	        <p>
	        	<div class="col-lg-6">
	        	  <c:out value="${ line.numPage }" />
	        	</div>	     
	        	<div class="col-lg-6">
	        	  <c:out value="${line.time }" />
	        	</div>
	        	<c:forEach items="${ line.original }" var="line1" varStatus="status">
	        	   <div class="col-lg-6">
	        	     <c:out value="${ line1 }" />
	        	   </div>
	        	   <div class="col-lg-6">
	        		 <input type="text" name="paragraphe${ line.numPage }linge${ status.index }" id="paragraphe${ line.numPage }linge${ status.index }" value="${ line.traduit[status.index] }" size="75" />
	        	   </div>
	       		</c:forEach>
	        <p>    	
	    	</c:forEach>
   
     </form>
      </div>
</body>
</html>