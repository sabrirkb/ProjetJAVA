<form action="index.php?uc=medicaments" method="post">
    <div id="contenu" align="center" style="background-color: lightsteelblue; color: white">
        <h1>Pharmacopee</h1>
        <br>
    <table style="width: 100%; background-color: transparent; color: white; font-weight: bold">
            <tbody style="consultation">
                  
    <?php /*
// Récupère les informations de chaque médicament
   foreach ($lesMedicaments as $unMedicament) { */
               $depot = "unDépot"; /*= $unMedicament['med_depotlegal']; */
               $nom = "unNom"; /* $unMedicament['med_nomcommercial']; */
               $famille = "uneFamille"; /* $unMedicament['fam_code']; */
               $compo = "uneCompo"; /* $unMedicament['med_composition']; */
               $effets = "desEffets"; /* $unMedicament['med_effets']; */
               $contre = "uneContre"; /* $unMedicament['med_contreindic']; */
               $prix = "unPrix"; /* $unMedicament['med_prixechantillon']; */
    ?>
                <tr>
                    <td>D&Eacute;POT LEGAL</td>
                    <td>
                        <input size="20" list="medicament" type="text" name="depot-medicament" autocomplete="off" disabled value="<?php echo $depot;?>">
                    </td>
                </tr>
                <tr>
                    <td>NOM COMMERCIAL</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="nom-medicament" autocomplete="off" disabled value="<?php echo $nom;?>">
                    </td>
                </tr>
                <tr>
                    <td>FAMILLE</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="famille-medicament" autocomplete="off" disabled value="<?php echo $famille;?>">
                    </td>
                </tr>
                <tr>
                    <td>COMPOSITION</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="compo-medicament" autocomplete="off" disabled><?php echo $compo;?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>EFFETS</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="effets-medicament" autocomplete="off" disabled><?php echo $effets;?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>CONTRE-INDICATIONS</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="contre-medicament" autocomplete="off" disabled><?php echo $contre;?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>PRIX &Eacute;CHANTILLON</td>
                    <td>
                        <input size="15" list="medicaments" type="text" name="prix-medicament" autocomplete="off" disabled value="<?php echo $prix;?>">
                    </td>
                </tr>
            </tbody>
            <?php		 /*
                }   */
            ?>	  
        </table>
        <br>
        <div align="center">
            <input type="submit" id="prec" formaction="index.php?uc=medicaments&do=lesMedicaments&action=precedent" method="post" name="Precedent" value="&laquo; Précédent">
            <input type="submit" id="suiv" formaction="index.php?uc=medicaments&do=lesMedicaments&action=suivant" method="post" name="Suivant" value="Suivant &raquo;"> 
        </div>
    </div>
</form>