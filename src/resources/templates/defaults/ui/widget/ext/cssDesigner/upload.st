<div class="CssUpload" style="position:relative;" uploadUrl="{if $uploadUrl != null}{$uploadUrl}{else}/ul{/if}" acceptType="{if $acceptType != null}{$acceptType}{else}*.jpg;*.gif;*.png;*.bmp{/if}" stylePath="{if $stylePath != null}{$stylePath}{else}/themes2/defaults{/if}">
    <nobr><input class="CssUploadInput" type="text" name="{$name}" value="{tostring bean=$value}" styleName="{$style}" />
    <input class="CssUploadFile" type="file" id="{$name}File" name="upload" disabled="true" style="display: block;" />
    </nobr>
    <div class="CssUploadProgress" id="{$name}Progress" style="width: 100%;position: absolute;left: 0;top: 0;height: 0;z-index: 2;"></div>
</div>