<form action="index.php?uc=medicaments" method="POST">
    <div id="contenu" align="center" style="background-color: lightsteelblue; color: white">
        <h1>Pharmacopee</h1>
        <br>
    <table style="width: 100%; background-color: transparent; color: white; font-weight: bold">
            <tbody style="consultation">
            <tr>
                    <td>D&Eacute;POT LEGAL</td>
                    <td>
                        <input size="20" list="medicament" type="text" name="depot-medicament" autocomplete="off" disabled value="<?php echo $_SESSION['Med_depot'];?>">
                    </td>
                </tr>
                <tr>
                    <td>NOM COMMERCIAL</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="nom-medicament" autocomplete="off" disabled value="<?php echo $_SESSION['Med_nom'];?>">
                    </td>
                </tr>
                <tr>
                    <td>FAMILLE</td>
                    <td>
                        <input size="30" list="medicaments" type="text" name="famille-medicament" autocomplete="off" disabled value="<?php echo $_SESSION['Med_famille'];?>">
                    </td>
                </tr>
                <tr>
                    <td>COMPOSITION</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="compo-medicament" autocomplete="off" disabled><?php echo $_SESSION['Med_compo'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>EFFETS</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="effets-medicament" autocomplete="off" disabled><?php echo $_SESSION['Med_effets'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>CONTRE-INDICATIONS</td>
                    <td>
                        <textarea rows="5" cols="60" list="medicaments" name="contre-medicament" autocomplete="off" disabled><?php echo $_SESSION['Med_contre'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>PRIX &Eacute;CHANTILLON</td>
                    <td>
                        <input size="15" list="medicaments" type="text" name="prix-medicament" autocomplete="off" disabled value="<?php echo $_SESSION['Med_prix'];?>">
                    </td>
                </tr>
            </tbody>
        </table>
        <br>
        <div align="center">
            <input type="submit" id="prec" formaction="index.php?uc=medicaments&do=lesMedicaments&action=precedent" method="POST" name="Precedent" value="&laquo; Précédent">
            <input type="button" id="ind" name="ind" value="&nbsp;<?php echo $_SESSION['index']; ?>&nbsp;" disabled style="background-color: white; color: black; font-weight: bold">
            <input type="submit" id="suiv" formaction="index.php?uc=medicaments&do=lesMedicaments&action=suivant" method="POST" name="Suivant" value="Suivant &raquo;"> 
        </div>
    </div>
</form>