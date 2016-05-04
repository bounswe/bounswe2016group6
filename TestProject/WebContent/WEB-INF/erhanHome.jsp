<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query Analysis</title>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet" type="text/css">
	
<style type="text/css">
#custom-search-input{
    padding: 3px;
    border: solid 1px #E4E4E4;
    border-radius: 6px;
    background-color: #fff;
}

#custom-search-input input{
    border: 0;
    box-shadow: none;
}

#custom-search-input button{
    margin: 2px 0 0 0;
    background: none;
    box-shadow: none;
    border: 0;
    color: #666666;
    padding: 0 8px 0 10px;
    border-left: solid 1px #ccc;
}

#custom-search-input button:hover{
    border: 0;
    box-shadow: none;
    border-left: solid 1px #ccc;
}

#custom-search-input .glyphicon-search{
    font-size: 23px;
}
</style>
<script type="text/javascript">
	function submitSearch() {
		$.get("", {
			queryTerm : $("#queryTermInput").val()
		}).done(function(data) {
			$("#result").html(data);
			alert("Data Loaded: " + data);
		});
	}
</script>

</head>

<body>
<div class="container">
	<div class="row">
        <div class="col-md-6">
    		<h2>Search the Data</h2>
            <div id="custom-search-input">
            	<form onsubmit="submitSearch();return false" id="searchForm" role="form">
                <div class="input-group col-md-12">
                    <input id="queryTermInput"type="text" class="form-control input-lg" name="queryTerm" placeholder="Enter Query Term..." />
                    <span class="input-group-btn">
                        <button class="btn btn-info btn-lg" type="submit">
                            <i class="glyphicon glyphicon-search"></i>
                        </button>
                    </span>
                </div>
                </form>
            </div>
            
            <h3 id="result"> Ready!</h1>
        </div>
	</div>
</div>

<div>
	
</div>

</body>
</html>