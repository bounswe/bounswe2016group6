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

	function printTable(dat) {
		var rows = dat.split("&&");
		var cols=rows[0].split("||");
		$("#excelDataTable").append('<tr/>');
		var row$ =$('<tr/>').append($('<th/>').html("Select"));
		for(var i = 0 ; i < cols.length ;i++){
			row$.append($('<th/>').html(cols[i]));
		}
		$("#excelDataTable").append(row$);
		 for (var i = 1 ; i < rows.length -1; i++) {	 
			  row$ = $('<tr/>').append($('<td/>').html("<input type=\"checkbox\" name=\"id\" value="+i+">"));
			 cols = rows[i].split("||");
			  for (var colIndex = 0 ; colIndex < cols.length ; colIndex++) {
				 var cellValue =cols[colIndex];
				 row$.append($('<td/>').html(cellValue));				
			}
			 $("#excelDataTable").append(row$);
		  }
	}
	 
	/// Sends the given search term to the servlet and prints the resulting data
	/// in a clickable table format.
	function submitSearch() {
		$.get("", {
			type : "queryData",
			input : $("#queryInput").val()
		}).done(function(data) { //print the resulting data in table format
			printTable(data);
		});
	}

	function submitCheckbox(type) {
		var inputs = document.getElementsByName("id"); //or document.forms[0].elements;
		var checked = []; //will contain all checked checkboxes
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked) {
			  checked.push(i);
			}
		}
		var str = "" + checked[0];
		for (var i = 1; i < checked.length; ++i) {
			str = str + " " + checked[i];
		}
		if (type === "save") {
			$.get("", {
				type : "insertData",
				input : str
			}).done(function(data) {
				if (data === "0") {
					alert("Error: Selected rows couldn't be saved");
				} else {
					alert("Success: Selected rows are saved");
				}
			});
		} else if (type === "delete") {
			$.get("", {
				type : "deleteData",
				input : str
			}).done(function(data) {
				if (data === "0") {
					alert("Error: Selected rows couldn't be deleted");
				} else {
					alert("Success: Selected rows are deleted");
				}
			});
		}
	}
	  
	function listData() {
		$.get("", {
			type : "listData"
		}).done(function(data) {
			if (data === "0") {
				alert("Error: Data couldn't be retrieved from the database");
			} else {
				printTable(data);
			}
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
                    <input id="queryInput"type="text" class="form-control input-lg" name="queryInput" placeholder="Query term" />
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
			<button onclick="submitCheckbox('save')" >
				Save
			</button>
			<button onclick="submitCheckbox('delete')" >
				Delete
			</button>
			<button onclick="listData()" >
				List Database
			</button>
        </div>
	</div>
</div>
</body>
</html>