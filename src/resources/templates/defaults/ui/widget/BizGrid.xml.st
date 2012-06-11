<?xml version='1.0' encoding='UTF-8'?>
<rows>
	<page>{$data.pageIndex}</page>
	<total>{$data.totalPage}</total>
	<records>{$data.totalRecord}</records>{foreach from=$data.result item="row"}
	<row>
		<cell>{prop bean=$row name="id" pattern="#"}</cell>{foreach from=$ui.fields item="fld"}
		<cell>{prop bean=$row name=$fld.propName pattern=$fld.pattern}</cell>{/foreach}
	</row>{/foreach}
</rows>
