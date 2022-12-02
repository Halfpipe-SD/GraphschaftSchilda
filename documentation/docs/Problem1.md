# Problem 1 - "Straßen müssen her!"

Lange Zeit gab es in der Graphschaft Schilda einen Reformstau, kein Geld floss mehr in die Infrastruktur. Wie es kommen musste, wurde der Zustand der Stadt zusehends schlechter, bis die Bürger der Graphschaft den Aufbau Ihrer Stadt nun endlich selbst in die Hand nahmen. Zunächst einmal sollen neue Straßen gebaut werden. Zur Zeit gibt es nur einige schlammige Wege zwischen den Häusern. Diese sollen nun gepflastert werden, so dass von jedem Haus jedes andere Haus erreichbar ist. Da die Bürger der Stadt arm sind, soll der Straßenbau insgesamt möglichst wenig kosten. Die Bürger haben bereits einen Plan mit möglichen Wegen erstellt. Ihre Aufgabe ist nun, das kostengünstigste Wegenetz zu berechnen, so dass alle Häuser miteinander verbunden sind (nehmen Sie dabei pro Pflasterstein Kosten von 1 an):

## Modellierung des Problems

Das Problem lässt sich als Graphenmodell mit ungerichteten Kanten darstellen. Jedes Haus ist ein Knoten, die Straßen sind die Kanten. Die Kosten der Kanten sind die Kosten für die Pflastersteine. 

Um den Graph zu modellieren werden die Java-Bibliotheken `JGraphT` und `JGraphX` verwendet. Mit `JGraphT` wird der Graph als Datenstruktur modelliert. Mit `JGraphX` wird der Graph als Grafik dargestellt und auf dem Bildschirm dargestellt.

## Die Eingabe

Die Eingabe besteht aus einem Graphen, der aus Kanten und Knoten besteht. Diese werden aus einer `.json` Datei gelesen und in eine Insanz der Klasse `GraphData.java` geladen. Diese Insanz dient als Basis für die Berechnung des günstigsten Weges.


```json
{
  "directed_edges": false,
  "vertices": [
    {
      "label": "Wasserwerk"
    }, {
      "label": "Thoma"
    }
    ...
  ],
  "edges": [
    {
      "source": "Wasserwerk",
      "target": "Thoma",
      "weight": 15
    },
    ...
  ]
}
```

## Die Ausgabe

Die Ausgabe wird als Graph in einem Fenster dargestellt. Das Fenster besteht aus zwei Hälften. Auf der linken Seite wird der Eingabegraph dargestellt. Auf der rechten Seite wird der berechnete Graph dargestellt. 

![Problem1](images/Problem1.png)

## Der Alrogithmus

TODO Beschreibung MST mit Prim

## Die Laufzeit des Algorithmus

TODO Laufzeitberechnung

## Die Implementation des Algorithmus

```java
// Initialisiere alle Knoten mit ∞, setze den Vorgänger auf null
for (GraphVertex v : vertices) {
  v.setValue(Integer.MAX_VALUE);
  v.setPredecessor(null);
}

// Starte mit beliebigem Startknoten, Startknoten bekommt den Wert 0
GraphVertex start = vertices.get(6);
start.setValue(0);

// Speichere alle Knoten in einer geeigneten Datenstruktur Q
// -> Prioritätswarteschlange
PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(vertices.size(), new VertexComparator());
queue.addAll(vertices);

// Solange es noch Knoten in Q gibt...
while (!queue.isEmpty()) {
  // Wähle den Knoten aus Q mit dem kleinsten Schlüssel (v)
  GraphVertex vertex = queue.poll();

  // Speichere alle Nachbarn von v in neighbours
  ArrayList<GraphVertex> neighbors = GraphData.getNeighbors(vertex, vertices, edges);

  for (GraphVertex n : neighbors) {
    // Finde Kante zwischen v und n
    for (GraphEdge edge : GraphData.getEdgesBetweenTwoVertices(vertex, n, edges)) {
      // Wenn der Wert der Kante kleiner ist als der Wert des Knotens
      // prüfe ob der Knoten noch in Q ist
      if (edge.getWeight() < n.getValue() && queue.contains(n)) {
        // Speichere v als vorgänger von n und passe wert von n an
        n.setValue((int) edge.getWeight());
        n.setPredecessor(vertex);
        // Aktualisiere die Prioritätswarteschlange
        queue.remove(n);
        queue.add(n);
      }
    }
  }
}
```