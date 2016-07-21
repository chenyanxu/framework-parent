#-----------------------------------------------------------------
# Use this file to add customized Bnd instructions for the bundle
#-----------------------------------------------------------------

Bundle-Activator: ${bundle.namespace}.internal.InitActivator
Import-Package:com.kalix.framework.core.api.persistence,\
               com.kalix.framework.core.api.biz,\
               <packageName>.entities,*
Bundle-Category: <namePrefix>