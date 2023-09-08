# Tesla-Tessie-Poweropti_Powerfox-Automation
This script is primarily designed for German households, as they typically have a standardized electricity meter. With digital electricity meters, you can monitor whether your solar power system is importing or exporting energy to the grid. This script checks this every 60 seconds. If the solar power produced by your household minus your household consumption is sufficient to charge your Tesla, the script will initiate charging when the Tesla is at your home location. (Please ensure that you enter your home address exactly as it appears in your Tesla App / Tessie App).

# Prerequisites:
0.) A computer that can run 24/7

1.) The Tessie App for starting/stopping the charging: Tessie Website: https://tessie.com

2.) A reader to monitor your solar power output: Poweropti by Powerfox: https://poweropti.powerfox.energy/

3.) The code for your digital electricity meter, like this one: YouTube Video: https://poweropti.powerfox.energy/

# Costs:
Tessie App: Available as a monthly subscription (€6) or a one-time lifetime purchase (€230)

Powerfox: €90

# How to Run:
Download this GitHub repository onto a computer that runs 24/7.

Navigate to the folder where you saved the download.

Locate the Main.java file.

Open Main.java with a text editor.

Update the corresponding values like AUTHORIZATION, YOUR_AUTHENTICATION_TOKEN (from Powerfox), VRN (Vehicle Registration Number: see in your Tesla App), BEARER (see Tessie website: Tessie Developer Reference (log in to see the String)), and HOME_ADDRESS.

Save the file.

Copy the path where the Main.java file is located (e.g., C:\Users\YOUR_USERNAME\Documents\Visual Studio Code Projects\TeslaTessiePoweroptiScript\folderOne\src\main\java).

Open the Command Prompt (cmd).

Type cd, press Ctrl+V, (now it should say cd C:\......\src\main\java), write 'Main.java', and then press Enter

The script should now start running. You can minimize the Command Prompt window, but do not close it.

Finish.

# Please note:
If you need to charge your Tesla even when your solar panel doesn't produce enough energy / when your household uses up too much energy, then please close the Terminal window. Otherwise it will stop charging after 60 seconds again.
