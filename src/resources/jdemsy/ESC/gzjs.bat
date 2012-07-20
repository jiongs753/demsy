cd E:\DEMSY.V2\src\resources\jdemsy\ESC

REM -------------- start package javascript --------------

type ..\src\js\jdemsy.core.js > jdemsy.src.js
type ..\src\js\jdemsy.nls.zh.js >> jdemsy.src.js
type ..\src\js\jdemsy.utils.js >> jdemsy.src.js
type ..\src\js\jdemsy.parse.js >> jdemsy.src.js
#type ..\src\js\jdemsy.alerts.js >> jdemsy.src.js
#type ..\src\js\jdemsy.draggable.js >> jdemsy.src.js
#type ..\src\js\jdemsy.droppable.js >> jdemsy.src.js
#type ..\src\js\jdemsy.resizable.js >> jdemsy.src.js
#type ..\src\js\jdemsy.accordion.js >> jdemsy.src.js
#type ..\src\js\jdemsy.tabs.js >> jdemsy.src.js
#type ..\src\js\jdemsy.datepicker.js >> jdemsy.src.js
type ..\src\js\jdemsy.dialog.js >> jdemsy.src.js
#type ..\src\js\jdemsy.viewgrid.js >> jdemsy.src.js
#type ..\src\js\jdemsy.flexigrid.js >> jdemsy.src.js

#Level 0 :: No compression
#Level 1 :: Comment removal
#Level 2 :: Whitespace removal
#Level 3 :: Newline removal
#Level 4 :: Variable substitution

cscript ESC.wsf -l 1 -ow jdemsy.all.js jdemsy.src.js
cscript ESC.wsf -l 2 -ow jdemsy.min.js jdemsy.all.js
#cscript ESC.wsf -l 3 -ow jdemsy.onerow.js jdemsy.nospace.js
#cscript ESC.wsf -l 4 -ow jdemsy.min.js demsy.onerow.js