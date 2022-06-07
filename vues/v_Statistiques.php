<div id="contenu">
    <h1>Statistiques de <?php echo $_SESSION['STAT_TYPELIEU'] . " : " . strtoupper($_SESSION['localite']); ?></h1>
	<div style="background-color: white; color: black; padding: 2px; border: 1px solid black">
	<h3>Taux concernant <?php echo $_SESSION['STAT_TYPELIEU'];?> (sur l'ensemble des données GSB)</h3>
    <div class="pie animate" style="--p:<?php echo $_SESSION['STAT_VISITE_POURCENT'];?>"> <?php echo $_SESSION['STAT_VISITE_POURCENT'];?>%</div>
<div class="pie animate" style="--p:<?php echo $_SESSION['STAT_MEDECIN_POURCENT'];?>;--c:darkblue;--b:10px"> <?php echo $_SESSION['STAT_MEDECIN_POURCENT'];?>%</div>
<div class="pie animate no-round" style="--p:<?php echo $_SESSION['STAT_VISITEURS_POURCENT'];?>;--c:purple;--b:15px"> <?php echo $_SESSION['STAT_VISITEURS_POURCENT'];?>%</div>
<div class="pie animate no-round" style="--p:<?php echo $_SESSION['STAT_DELEGUES_POURCENT'];?>;--c:orange;"> <?php echo $_SESSION['STAT_DELEGUES_POURCENT'];?>%</div>
<div class="pie animate" style="--p:<?php echo $_SESSION['STAT_RESPONSABLES_POURCENT'];?>;--c:lightgreen"> <?php echo $_SESSION['STAT_RESPONSABLES_POURCENT'];?>%</div>
<br>
<br>

<table id="statistiques" style="color:black; text-align: left; padding-left: 15%">
	<tr style="color:black; text-align: left; padding-left: 30%">
		<td>
			<text style="color:darkred;">■</text> Visites effectuées dans <?php echo $_SESSION['STAT_TYPELIEU'];?>
		</td>
		<td>
			<text style="color:darkblue;">■</text> Médécins concernés par <?php echo $_SESSION['STAT_TYPELIEU'];?>
		</td>
	</tr>
	<tr style="color:black; text-align: left; padding-left: 30%">
		<td>
			<text style="color:purple;">■</text> Visiteurs concernés par <?php echo $_SESSION['STAT_TYPELIEU'];?>
		</td>
		<td>
			<text style="color:orange;">■</text> Délégués concernés par <?php echo $_SESSION['STAT_TYPELIEU'];?>
		</td>
	</tr>
	<tr>
		<td>
			<text style="color:lightgreen;">■</text> Responsables concernés par <?php echo $_SESSION['STAT_TYPELIEU'];?>
		</td>
	</tr>
</table>
<br>

</div>

<?php
$dataPoints = array(
	array("label"=> "Visites", "y"=> $_SESSION['STAT_VISITE']),
	array("label"=> "Médecins", "y"=> $_SESSION['STAT_MEDECIN']),
	array("label"=> "Visiteurs", "y"=> $_SESSION['STAT_VISITEURS']),
	array("label"=> "Délégués", "y"=> $_SESSION['STAT_DELEGUES']),
	array("label"=> "Responsables", "y"=> $_SESSION['STAT_RESPONSABLES'])
);
?>

<br>

<script>
window.onload = function () {
 
var chart = new CanvasJS.Chart("chartContainer", {
	animationEnabled: true,
	theme: "light2", // "light1", "light2", "dark1", "dark2"
	title: {
		text: "Chiffres concernant les visites de <?php echo $_SESSION['STAT_TYPELIEU'];?>"
	},
	axisY: {
		title: "Quantité"
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

