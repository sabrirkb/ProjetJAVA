<div id="contenu">
        <h1>Praticiens</h1>
        <br>
        <form action="" method="POST">
    
    <table style="width: 100%; background-color: transparent; color: white; font-weight: bold">
            <tbody style="consultation">
            <tr>
                    <td>NOM</td>
                    <td>
                        <input type="text" name="nom-praticien" autocomplete="off" readonly style="margin: auto; width: 40%" value="<?php echo strtoupper($_SESSION['Prat_nom']);?>">
                    </td>
                </tr>
                <tr>
                    <td>PR&Eacute;NOM</td>
                    <td>
                        <input type="text" name="prenom-praticien" autocomplete="off" readonly style="margin: auto; width: 25%" value="<?php echo $_SESSION['Prat_prenom'];?>">
                    </td>
                </tr>
                <tr>
                    <td>NUM&Eacute;RO DU PRATICIEN</td>
                    <td>
                        <input type="text" name="num-praticien" autocomplete="off" readonly style="margin: auto; width: 10%" value="<?php echo strtoupper($_SESSION['Prat_num']);?>">
                    </td>
                </tr>
                <tr>
                    <td>ADRESSE</td>
                    <td>
                        <textarea type="text" name="adresse-praticien" autocomplete="off" readonly style="margin: auto; width: 35%"><?php echo $_SESSION['Prat_adresse'];?></textarea>
                    </td>
                </tr>
                <tr>
                    <td>CODE POSTAL</td>
                    <td>
                        <input type="text" name="cp-praticien" autocomplete="off" readonly style="margin: auto; width: 10%" value="<?php echo $_SESSION['Prat_cp'];?>">
                    </td>
                </tr>
                <tr>
                    <td>VILLE</td>
                    <td>
                        <input type="text" name="ville-praticien" autocomplete="off" readonly style="margin: auto; width: 40%" value="<?php echo $_SESSION['Prat_ville'];?>">
                    </td>
                </tr>
                <tr>
                    <td>COEFFICIENT DE NOTORI&Eacute;T&Eacute;</td>
                    <td>
                        <input type="text" name="coef-praticien" autocomplete="off" readonly style="margin: auto; width: 15%" value="<?php echo $_SESSION['Prat_coef'];?>">
                    </td>
                </tr>
                <tr>
                    <td>CODE TYPE</td>
                    <td>
                        <input type="text" name="codetype-praticien" autocomplete="off" readonly style="margin: auto; width: 10%; text-align: left" value="<?php echo $_SESSION['Prat_codetype'];?>">
                    </td>
                </tr>
            </tbody>
        </table>
        <br>
        <div class="nav-cons-med" align="center">
            <button class="btn-cons-med" type="button" onclick="window.location.href='index.php?uc=praticiens&action=precedent'">&laquo; Précédent</button>
            <span>
                <input class="btn-cons-med" size="2" type="text" name="updateIndex" value="<?php echo $_SESSION['Prat_index']; ?>" style="text-align: center;">
                <strong>
                    /&nbsp;<?php echo $pdo->getMaxPraticiens();?>&nbsp;
                </strong>
                <input class="btn-cons-med" type="submit" formaction="index.php?uc=praticiens&action=custom" method="POST" hidden>
            </span>
            <button class="btn-cons-med" type="button" onclick="window.location.href='index.php?uc=praticiens&action=suivant'">Suivant &raquo;</button>
        </div>
</form>

</div>