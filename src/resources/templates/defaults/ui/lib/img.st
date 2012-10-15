{if $img != null}
{isimg assign="isimg" bean=$img}
{if $isimg}
{if $block.imageWidth !=null && $block.imageHeight != null}{thumbs assign="logoimg" src=$img width=$block.imageWidth height=$block.imageHeight autoCut=$block.autoCutImage}{/if}
<img src="{if $imgDomain !=null}{$imgDomain}{/if}{if $logoimg!=null}{$logoimg}{else}{$img}{/if}" />
{else}
<div id="media{$ui.id}"></div>
<script>
$(document).ready(function(){
    $('#media{$ui.id}').flash(
    	{
            src: '{$img}',
            width: {if $block.imageWidth == null}{$block.position.width}{else}{$block.imageWidth}{/if},
            height: {if $block.imageHeight == null}{$block.position.height}{else}{$block.imageHeight}{/if}
        },{
            expressInstall: true
        }
    );
});
</script>
{/if}
{/if}