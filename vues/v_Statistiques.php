<div id="contenu">
    <h1>Statistiques de <?php echo $_SESSION['STAT_TYPELIEU'] . strtoupper($_SESSION['localite']); ?></h1>
    <br>
	<h3>Répartition des ...</h3>
    <div class="pie" style="--p:80"> 80% <br> des salariés</div>
<div class="pie" style="--p:40;--c:darkblue;--b:10px"> 40% des trucs</div>
<div class="pie no-round" style="--p:60;--c:purple;--b:15px"> 60% des machins</div>
<div class="pie animate no-round" style="--p:80;--c:orange;"> 80%</div>
<div class="pie animate" style="--p:90;--c:lightgreen"> 90%</div>

<?php
$dataPoints = array(
	array("label"=> "Education", "y"=> 284935, "indexLabel"=> "Highest"),
	array("label"=> "Entertainment", "y"=> 256548),
	array("label"=> "Lifestyle", "y"=> 245214),
	array("label"=> "Business", "y"=> 233464),
	array("label"=> "Music & Audio", "y"=> 200285),
	array("label"=> "Personalization", "y"=> 194422),
	array("label"=> "Tools", "y"=> 180337),
	array("label"=> "Books & Reference", "y"=> 172340),
	array("label"=> "Travel & Local", "y"=> 118187),
	array("label"=> "Puzzle", "y"=> 107530, "indexLabel"=> "Lowest")
);
?>

<br>

<script>
window.onload = function () {
 
var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2", // "light1", "light2", "dark1", "dark2"
	title: {
		text: "Titre"
	},
	axisY: {
		title: "Axe Y"
	},
	data: [{
		type: "column",
		dataPoints: <?php echo json_encode($dataPoints, JSON_NUMERIC_CHECK); ?>
	}]
});
chart.render();
 
}
</script>

<div id="chartContainer" style="position: relative; height:30vh; width:70vw"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

</div>                             

