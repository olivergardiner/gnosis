		<div class="indexContainer">
			<#if entities?size gt 0>
				<h2 title="Entities">Entities</h2>
				<ul title="Entities">
				<#list entities?sort_by("entityName") as entity>
					<li><a href="javascript:;" onclick="changeNode('${entity.packagePath}/${entity.entityName}.html')"><#if entity.isAbstract><i></#if>${entity.entityName}<#if entity.isAbstract></i></#if></a></li>
				</#list>
				</ul>
			</#if>
			<#if referenceEntities?size gt 0>
				<h2 title="ReferenceEntities">Reference Entities</h2>
				<ul title="ReferenceEntities">
				<#list referenceEntities?sort_by("entityName") as entity>
					<li><a href="javascript:;" onclick="changeNode('${entity.packagePath}/${entity.entityName}.html')"><#if entity.isAbstract><i></#if>${entity.entityName}<#if entity.isAbstract></i></#if></a></li>
				</#list>
				</ul>
			</#if>
			<#if complexEntities?size gt 0>
				<h2 title="ComplexEntities">Complex Entities</h2>
				<ul title="ComplexEntities">
				<#list complexEntities?sort_by("entityName") as entity>
					<li><a href="javascript:;" onclick="changeNode('${entity.packagePath}/${entity.entityName}.html')"><#if entity.isAbstract><i></#if>${entity.entityName}<#if entity.isAbstract></i></#if></a></li>
				</#list>
				</ul>
			</#if>
		</div>