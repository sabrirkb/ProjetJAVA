<div id="contenu">
    <h1>Statistiques de <?php echo $_SESSION['STAT_TYPELIEU'] . strtoupper($_SESSION['localite']); ?></h1>
    <br>
	<h3>Répartition des ...</h3>
    <div class="pie" style="--p:80"> 80% <br> des salariés</div>
<div class="pie" style="--p:40;--c:darkblue;--b:10px"> 40% des trucs</div>
<div class="pie no-round" style="--p:60;--c:purple;--b:15px"> 60% des machins</div>
<div class="pie animate no-round" style="--p:80;--c:orange;"> 80% bidule</div>
<div class="pie animate" style="--p:90;--c:lightgreen"> 90% chouette</div>

<?php
$dataPoints = array(
	array("label"=> "Salariés", "y"=> 17, "indexLabel"=> "Highest"),
	array("label"=> "Truc", "y"=> 9),
	array("label"=> "Machin", "y"=> 8),
	array("label"=> "Bidule", "y"=> 7),
	array("label"=> "Chose", "y"=> 6),
	array("label"=> "Chouette", "y"=> 5),
	array("label"=> "Item", "y"=> 7),
	array("label"=> "Libellé", "y"=> 11),
	array("label"=> "Nom", "y"=> 13),
	array("label"=> "Texte", "y"=> 2, "indexLabel"=> "Lowest")
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

