//Варианты работы с онтологией (на примерах):
//#1 (через OWLOntology)
System.out.println("TEST:" + ontology.containsObjectPropertyInSignature(predicate)); //!!!
		
//#2 (через OWLDataFactory)
OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(IRI.create(internalOnt + "#" + "existentiallyDependentOfQua"));
OWLSymmetricObjectPropertyAxiom test  = factory.getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
System.out.println("TEST:" + ontology.containsAxiom(test));
		

//#3 (через EntitySearcher)
System.out.println("TEST:" + EntitySearcher.);
		
//#4 (через Reasoner)
OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);        
reasoner.

//#5 (через конкретный объект)
OWLObjectProperty objectProperty = factory.getOWLObjectProperty(predicate);
System.out.println(objectProperty.);

---		

OWLClass owlClass = factory.getOWLClass(object);
		OWLIndividual ind = factory.getOWLNamedIndividual(subject);
		->factory.getSWRLClassAtom
		->SWRLAtom swrlClass = factory.getSWRLRule(owlClass, arg1)
		
		addAxiom(factory.getSWRLRule(owlClass, ind));

---

//получение всех классов в онтологии и так далее
 File file = new File("Ontology.owl");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;

        try {
            ontology = manager.loadOntologyFromOntologyDocument(file);

            Set<OWLClass> classes;
            Set<OWLObjectProperty> prop;
            Set<OWLDataProperty> dataProp;
            Set<OWLNamedIndividual> individuals;

            classes = ontology.getClassesInSignature(); //!!!
            prop = ontology.getObjectPropertiesInSignature();
            dataProp = ontology.getDataPropertiesInSignature();
            individuals = ontology.getIndividualsInSignature();
            //configurator = new OWLAPIOntologyConfigurator(this);            

            System.out.println("Classes");
            System.out.println("--------------------------------");
            for (OWLClass cls : classes) {
                System.out.println("+: " + cls.getIRI().getShortForm());

                System.out.println(" \tObject Property Domain");
                for (OWLObjectPropertyDomainAxiom op : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {                        
                        if (op.getDomain().equals(cls)) {   
                            for(OWLObjectProperty oop : op.getObjectPropertiesInSignature()){
                                 System.out.println("\t\t +: " + oop.getIRI().getShortForm());
                            }
                            //System.out.println("\t\t +: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
                        }
                    }

                    System.out.println(" \tData Property Domain");
                    for (OWLDataPropertyDomainAxiom dp : ontology.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
                        if (dp.getDomain().equals(cls)) {   
                            for(OWLDataProperty odp : dp.getDataPropertiesInSignature()){
                                 System.out.println("\t\t +: " + odp.getIRI().getShortForm());
                            }
                            //System.out.println("\t\t +:" + dp.getProperty());
                        }
                    }

            }

        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(OntologyAPI.class.getName()).log(Level.SEVERE, null, ex);
        }


---

1 OWLClass mann = df.getOWLClass(IOR+"#Man");
2 // mann with two "n" to avoid confusion with the OWLOntologyManager
3 OWLClass woman = df.getOWLClass(IOR+"#Woman");
4 OWLSubClassOfAxiom m_sub_w = df.getOWLSubClassOfAxiom(mann, woman);
5 o.add(m_sub_w);

---


//CLASS + HASSOMETHING + CLASS
//чем это разница с уже написаным методом для individuals?

OWLOntologyManager m = create();
OWLOntology o = m.createOntology(example_iri);
// all Heads have parts that are noses (at least one)
// We do this by creating an existential (some) restriction

OWLObjectProperty hasPart = df.getOWLObjectProperty(IRI.create(example_iri + "#hasPart"));
OWLClass nose = df.getOWLClass(IRI.create(example_iri + "#Nose"));

// Now let’s describe the class of individuals that have at
// least one part that is a kind of nose
OWLClassExpression hasPartSomeNose = df.getOWLObjectSomeValuesFrom(hasPart, nose);
OWLClass head = df.getOWLClass(IRI.create(example_iri + "#Head"));
// Head subclass of our restriction
OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(head, hasPartSomeNose);
m.applyChange(new AddAxiom(o, ax));

---

			//EntitySearcher.
			//factory.getpropertyaxiom
				
---


		System.out.println("Classes: ");
		System.out.println(classes);
		System.out.println("Individuals: ");
		System.out.println(individuals);
		
		System.out.println("Is entity1 a class?");
		System.out.println(classes.contains(owlClass1));
		System.out.println("Is entity2 a class?");
		System.out.println(classes.contains(owlClass2));
		
		System.out.println("Is entity1 an ind?");
		System.out.println(individuals.contains(owlIndividual1));
		System.out.println("Is entity2 an ind?");
		System.out.println(individuals.contains(owlIndividual2));
		
		
		OWLEntity tmp = factory.getOWLEntity(EntityType.CLASS, entity1);
		tmp.getEntityType();
		System.out.println(tmp.getEntityType());
		
		
		OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		
		
		System.out.println(factory.getOWLClass(entity1));
		System.out.println(factory.getOWLClass(entity2));
		System.out.println(factory.getOWLClass(entity1).isOWLClass());
		System.out.println(factory.getOWLClass(entity2).isOWLClass());


---


		OWLEntity.getIRI().getShortForm().


---


			// Part for test
			System.out.println("Output: " + ind.getClassesInSignature());
			Set<OWLClass> unsatisfiable = ind.getClassesInSignature();
			if (!unsatisfiable.isEmpty()) {
				for (OWLClass cls : unsatisfiable) {
					System.out.println("    " + cls);
				}
			} else {
				System.out.println("It is empty!!!");
			}
			
			Set<OWLDataProperty> unsatisfiable2 = ind.getDataPropertiesInSignature();
			if (!unsatisfiable2.isEmpty()) {
				for (OWLDataProperty cls : unsatisfiable2) {
					System.out.println("    " + cls);
				}
			} else {
				System.out.println("It is empty!!!2");
			}
			
			Set<OWLDatatype> unsatisfiable3 = ind.getDatatypesInSignature();
			if (!unsatisfiable3.isEmpty()) {
				for (OWLDatatype cls : unsatisfiable3) {
					System.out.println("    " + cls);
				}
			} else {
				System.out.println("It is empty!!!3");
			}
			
			
			System.out.println("TEST: " + ind.getEntityType());
			Set<OWLNamedIndividual> unsatisfiable4 = ind.getIndividualsInSignature();
			if (!unsatisfiable4.isEmpty()) {
				for (OWLNamedIndividual cls : unsatisfiable4) {
					System.out.println("    " + cls);
				}
			} else {
				System.out.println("It is empty!!!");
			}
			
			System.out.println("TEST: " + ind.getAnnotationPropertiesInSignature());
			System.out.println("TEST: " + ind.getObjectPropertiesInSignature());
			
			
