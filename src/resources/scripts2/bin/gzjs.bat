cd E:\DEMSY.V2\src\resources\scripts2\bin

REM -------------- start package javascript --------------

type ..\src\demsy.nls.zh.js > demsyESC.js
type ..\src\demsy.config.js >> demsyESC.js
type ..\src\demsy.alerts.js >> demsyESC.js
type ..\src\demsy.utils.js >> demsyESC.js
type ..\src\demsy.core.js >> demsyESC.js
type ..\src\demsy.draggable.js >> demsyESC.js
type ..\src\demsy.droppable.js >> demsyESC.js

cscript ESC.wsf -l 1 -ow demsyESC1.js demsyESC.js
cscript ESC.wsf -l 2 -ow demsyESC2.js demsyESC1.js
cscript ESC.wsf -l 3 -ow demsyESC3.js demsyESC2.js
cscript ESC.wsf -l 4 -ow demsyESC4.js demsyESC3.js

type demsyESC3.js > demsy.min.js

#gzip -f demsy.min.js
#copy demsy.min.js.gz demsy.min.gzjs /y

del demsyESC*.js
del demsy.min.js.gz