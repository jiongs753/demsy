<span class="QrSpinner"><input class="QrSpinnerInput" id="{$name}#input" name="{$name}" value="{$value}" size="{$size}" onkeyup="QrSpinner.onKeyup('{$name}')" /><span class="QrSpinnerButton"><img src="{$designerPath}/img/spinner-normal.gif" align="top" id="{$name}#button" onmousemove="QrSpinner.onHover(event,'{$name}')" onmouseout="QrSpinner.onOut(event,'{$name}')" onmousedown="QrSpinner.onDown(event,'{$name}')" /></span></span>
<script>
connectCSS(new QrSpinner("{$name}"), "{$style}");
</script>