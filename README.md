Travel constructor project for university project
=================================================
"Использование онтологий для анализа и оптимизации 
бизнес-процессов с учетом пожеланий клиента и ограничений заказчика"

Env installing
--------------
```
1) Установить Node JS включая доп утилиты (тулзы для компиляции нативного кода не надо - там отдельная галка при установке) 
2) Зайти в консоль, npm -v проверить, что всё ок
3) Перейти в папку frontend
4) npm install - установятся зависимости
5) npm run serve - запустится фронт, в консоли появится url
P.S. Лучше сначала запускать бэк, т.к. фронт сам найдёт свободный порт, а джава хочет дефолтный 8080)
```

Parts of project:
-----------------
- ontAgent
- API backend
- web frontend
- codified ontology `travel_constructor\src\main\resources\public\boldino.owl`

Notes
-----
- modified OntAgent based on previous original authors projects
