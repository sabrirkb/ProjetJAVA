<div id="contenu">
        <h1>Pharmacopee</h1>
        <br>
        <form action="" method="POST">
    
    <table style="width: 100%; background-color: transparent; color: white; font-weight: bold">
            <tbody style="consultation">
            <tr>
                    <td>D&Eacute;POT LEGAL</td>
                    <td>
                        <input list="medicament" type="text" name="depot-medicament" autocomplete="off" disabled style="margin: auto; width: 30%" value="<?php echo $_SESSION['Med_depot'];?>">
                    </td>
                </tr>
                <tr>
                    <td>NOM COMMERCIAL</td>
                    <td>
                        <input list="medicaments" type="text" name="nom-medicament" autocomplete="off" disabled style="margin: auto; width: 40%" value="<?php echo $_SESSION['Med_nom'];?>">
                    </td>
                </tr>
                <tr>
                    <td>FAMILLE</td>
                    <td>
                        <input list="medicaments" type="text" name="famille-medicament" autocomplete="off" disabled style="margin: auto; width: 35%" value="<?php echo $_SESSION['Med_famille'];?>">
                    </td>
                </tr>
                <tr>
                    <td>COMPOSITION</td>
                    <td>
                        <textarea rows="5" list="medicaments" name="compo-medicament" autocomplete="off" disabled style="margin: auto; width: 100%"><?php echo $_SESSION['Med_compo'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>EFFETS</td>
                    <td>
                        <textarea rows="5" list="medicaments" name="effets-medicament" autocomplete="off" disabled style="margin: auto; width: 100%"><?php echo $_SESSION['Med_effets'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>CONTRE-INDICATIONS</td>
                    <td>
                        <textarea rows="5" list="medicaments" name="contre-medicament" autocomplete="off" disabled style="margin: auto; width: 100%"><?php echo $_SESSION['Med_contre'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>PRIX &Eacute;CHANTILLON</td>
                    <td>
                        <input list="medicaments" type="text" name="prix-medicament" autocomplete="off" disabled style="margin: auto; width: 15%; text-align: right" value="<?php echo $_SESSION['Med_prix'];?>">
                        €
                    </td>
                </tr>
            </tbody>
        </table>
        <br>
        <div class="nav-cons-med" align="center">
            <button class="btn-cons-med" type="button" onclick="window.location.href='index.php?uc=medicaments&do=lesMedicaments&action=precedent'">&laquo; Précédent</button>
            <span>
                <input class="btn-cons-med" size="2" type="text" name="updateIndex" value="<?php echo $_SESSION['Med_index']; ?>" style="text-align: center;">
                <strong>
                    /&nbsp;<?php echo $pdo->getMaxMedicaments();?>&nbsp;
                </strong>
                <input class="btn-cons-med" type="submit" formaction="index.php?uc=medicaments&do=lesMedicaments&action=custom" method="POST" hidden>
            </span>
            <button class="btn-cons-med" type="button" onclick="window.location.href='index.php?uc=medicaments&do=lesMedicaments&action=suivant'">Suivant &raquo;</button>
        </div>
</form>

</div>