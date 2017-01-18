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

####Themes
The syntax highlighter is data-driven. It uses the file `default_theme.txt`
to associate styles with lexical structures. A more detailed description of
the theme file format can be found in `default_theme.txt` to supplement this
general overview:

  1. Leading and trailing whitespace is allowed, as well as empty lines
  2. Comments
  
         # start line with pound sign
    
  3. Variables can be used as substitutions for values and can contain any
     non-whitespace characters, so long as the name begins with `$`
     
         $red 255 0 0
   
  4. Apply styles to a code structure by first naming it and then supplying
     a semicolon separated list of attributes and values. If there is only
     one attribute then a semicolon is not necessary.
     
         #(token name)      (attribute)   (values)
         tag-start            color       0 0 255
         tag-name             color 0 0 255; background $red; italic
      
  5. Although this is not a cascading stylesheet, it is possible to set
     styles for a bunch of contexts(and there are a lot) at once. Just name
     the root of the context followed by a dash and then a `*`(star). Styles
     are not inherited though, so if you define styles for a context using
     a more descriptive name(longer) then it will override all previous ones.
     
     ```
     #applies to tag-start, tag-name, tag-content, tag-special and tag-end
     tag-* color $blue
     #override
     tag-start color 100
     
     attribute-* color $purple
     #override
     attribute-value-* color 87 228 228
     attribute-value-quoted-* color $blue
     attribute-value-quoted-double color $blue; italic
     ```

####Features in developmentt
  + Full syntax support for HTML
  + Cutting and copying a region. Currently, <b>Copy</b> copies the text in the entire text area.
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
