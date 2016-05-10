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

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.1/bootstrap-table.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.1/bootstrap-table.min.js"></script>

	<!-- Search bar JSS -->
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

//
$(function () {
	//Hide the result div
	$("#resultTableDiv").hide();
	//The result table's variable
	$table = $("#movieTable");
	//When user wants to save some rivers 
	$('#submitDatabase').click(
				function() {
					var riverNames = [];
					//Get all river names from selected entries.
					$.each($table.bootstrapTable('getAllSelections'), function(
							key, value) {
						//add them to rivernames array
						riverNames.push(value["riverLabel"]);
					});
					//Debug
					console.log(riverNames);
					
					//send a request to servlet with type saveData and data string
					$.post('', {
						type : "saveData",
						dataList : JSON.stringify(riverNames)
					}).done(function(data) {
						alert(data); // Alert the result of operation
					});
				});
	
	});
	
	var queryResultData = [];
	//When user searches for a string
	function submitSearch() {
		//Send a request to server with search term
		$.get("", {
			queryTerm : $("#queryTermInput").val()
		}).done(function(data) {
			//Store the response json in query result data
			queryResultData = jQuery.parseJSON(data);
			//Debug
			console.log(queryResultData);
			//Alert the user
			alert("Data retrieval Complete");
			//Initialize the table
			parseData();
		});
	}

	function parseData() {
		console.log("Parsing Data!")
		//Make the result div visible
		$("#resultTableDiv").show();
		//Initialize table with result data.
		$("#movieTable").bootstrapTable({
			data : queryResultData
		});
		//load the data if it is changed.
		$("#movieTable").bootstrapTable('load', queryResultData);
	}
	
	
	

</script>

</head>

<body>
<div class="container">
	<div class="row">
        <div class="col-md-6">
    		<h2>Search the Data by entering a river name</h2>
    		<h4> Examples: Amazonas , Antas River</h4>
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
	<div id="resultTableDiv" class="row">

				<table id="movieTable" data-show-columns="true" data-show-refresh="true" data-click-to-select="true" data-show-export="true" data-pagination="true" data-show-pagination-switch="true" data-page-list="[10, 25, 50, 100, ALL]" data-filter-control="true" data-search="true">
					<thead>
						<tr>
							<th data-field="state" data-checkbox="true"></th>
							<th data-field="riverLabel" data-sortable="true" data-filter-control="input" >River name</th>
							<th data-field="Latitude" data-sortable="true" data-filter-control="input">Latitude</th>
							<th data-field="Longtitude" data-sortable="true" data-filter-control="input">Longtitude</th>
							<th data-field="Length" data-sortable="true" data-filter-control="input">Length</th>
						</tr>
					</thead>
				</table>
				
				<button id="submitDatabase" type="button" class="btn btn-default btn-lg" aria-label="Left Align">
							<span class="glyphicon glyphicon glyphicon-arrow-right" aria-hidden="true"></span>Add to Database
						</button>
			</div>

	</div>

</body>
</html>