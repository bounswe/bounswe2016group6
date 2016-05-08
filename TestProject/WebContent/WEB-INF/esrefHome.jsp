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
			type : "queryData",
			input : $("#queryInput").val()
		}).done(function(dat) { //print the resulting data in table format
			 var data = eval("(" + dat + ")");
			 var columns = data["head"]["vars"];
			 var myList = data["results"]["bindings"];
		 
			 //TODO: add table headers
			 $("#excelDataTable").append('<tr/>');
			 var row$ = $('<tr/>').append($('<th/>').html("Select"));
			 for (var i = 0; i < columns.length; ++i) {
				 row$.append($('<th/>').html(columns[i]));
				 //$("#excelDataTable").append($('<th/>').html(i));
			 }
			 $("#excelDataTable").append(row$);
			 for (var i = 0 ; i < myList.length ; i++) {
				 row$ = $('<tr/>').append($('<td/>').html("<input type=\"checkbox\" name=\"NAME\" value=\"VALUE\">"));
				 for (var colIndex = 0 ; colIndex < columns.length ; colIndex++) {
					 var cellValue = myList[i][columns[colIndex]]["value"];
					 if (cellValue == null) {
						 cellValue = "";
					 }
					 row$.append($('<td/>').html(cellValue));
				 }
				 $("#excelDataTable").append(row$);
		 	}
		});
	}
	function submitCheckbox() {
		$.get("", {
			type : "selectData",
			input : "blablabla"
		}).done(function(data) {
			//success or fail message
		});
	}
	function listData() {
		$.get("", {
			type : "listData"
		}).done(function(data) {
			//print the data in a table without checkboxes
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
                    <input id="queryInput"type="text" class="form-control input-lg" name="queryInput" placeholder="Enter Query Term..." />
                    <span class="input-group-btn">
                        <button class="btn btn-info btn-lg" type="submit">
                            <i class="glyphicon glyphicon-search"></i>
                        </button>
                    </span>
                </div>
                </form>
            </div>
            
            <form action="">
			<table id="excelDataTable" border="1"> </table>
			</form>
        </div>
	</div>
</div>

</body>
</html>
