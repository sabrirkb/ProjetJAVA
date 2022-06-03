<div id="contenu">
    <h1>Statistiques de <?php echo $_SESSION['STAT_TYPELIEU'] . strtoupper($_SESSION['localite']); ?></h1>
	<div style="background-color: white; color: black; padding: 2px; border: 1px solid black">
	<h3>Taux des visites concernant ma région sur l'ensemble des données GSB</h3>
    <div class="pie" style="--p:43"> 43%</div>
<div class="pie" style="--p:52;--c:darkblue;--b:10px"> 52%</div>
<div class="pie no-round" style="--p:37;--c:purple;--b:15px"> 37%</div>
<div class="pie animate no-round" style="--p:23;--c:orange;"> 23%</div>
<div class="pie animate" style="--p:59;--c:lightgreen"> 59%</div>
<br>
<br>
<table style="color:black; text-align: left; margin-left: 30%">
	<tr>
		<td>
	<text style="color:darkred;">■</text> Visites effectuées dans ma région
</td>
<td>
	<text style="color:darkblue;">■</text> Médécins concernés par ma région
	</td>
</tr>
	<tr>
		<td>
	<text style="color:purple;">■</text> Ventes du laboratoire Gyverny
</td>
<td>
	<text style="color:orange;">■</text> Ventes du laboratoire Swiss Kane&nbsp;&nbsp;&nbsp;
</td>
</tr>
<tr>
	<text style="color:lightgreen;">■</text> Ventes du laboratoire Bichat&nbsp;&nbsp;&nbsp;
</tr>
</table>
</div>

<?php
$dataPoints = array(
	array("label"=> "Visites", "y"=> 86),
	array("label"=> "Médecins", "y"=> 104),
	array("label"=> "Ventes Gyverny", "y"=> 74),
	array("label"=> "Ventes Swiss Kane", "y"=> 46),
	array("label"=> "Ventes Bichat", "y"=> 118/*, "indexLabel"=> "Lowest"*/)
);
?>

<br>

<script>
window.onload = function () {
 
var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2", // "light1", "light2", "dark1", "dark2"
	title: {
		text: "Chiffres concernant les visites de ma région"
	},
	axisY: {
		title: "Nombre"
	},
	data: [{
		type: "column",
		dataPoints: <?php echo json_encode($dataPoints, JSON_NUMERIC_CHECK); ?>
	}]
});
chart.render();
 
}
</script>

<div id="chartContainer" align="center" style="position: relative; height:50vh; width:100%; border: 1px solid black"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

</div>                             

