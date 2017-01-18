# APCS1-Final-Project

<h3> Text Editor (w/ HTML Viewing Capability) </h3>

<h4> A GUI text editor that allows you to view the HTML you create alongside. Offers syntax coloring and editing features.

<hr>
<h4>Features:</h4>
<ul>
<li>Open, save, or create new files</li>
<li>Basic customization of font</li>
<li>Preview your HTML with an updating visualizer</li>
<li>Auto-update for HTML Visualizer</li>
<li>Customizable theme files for syntax coloring</li>
</ul>

<h4>How to use:</h4>
To display the visualizer, enable <b>Properties » Show HTML Visualizer</b>
<br>
Once you have the visualizer enabled, you must open or save your current file
(if it's new). <br><br>
If you find that the auto-updates to the visualizer slows down the editor, 
you can disable auto-saving by toggling off <b>Properties » Enable Auto-save</b> <br><br>
You can also change font and font-size in <b>Properties</b> <br><br>
You can <b>Save</b> or <b>Open</b> files in <b>File</b>

####Features in developmentt
  + Syntax support for HTML
  + Cutting and copying a region. Currently, <b>Copy</b> copies the text in the entire region.
  + Tabs (for multiple files)
  + Manually set keybinds
  + Figure out how to recolor html code after deletions are made
  + Edit color schemes directly from the program
  + CSS syntax highlighting
  + Auto-indent selection

####Unresolved Bugs
  + Sometimes when a file is opened the syntax highlighting is off
  + After deleting some code the visualizer will update but the syntax highlighter will not
  + HTML comments are not colored properly all of the time
  + Copy feature (in menu) copies entire code instead of selected region in cursor.

<hr>
<h4>Compilation:</h4>
<i>Windows</i>: <br>
Compile and run by running the batch file:
```
$ run.bat
```
<br>
_Other_: <br>
Compile by typing:
```
$ javac Main.java
```
Then run:
```
$ java Main
```

<hr>
####Contributors:
<br>
Jeffrey Luo
<br>
Max Zlotskiy
