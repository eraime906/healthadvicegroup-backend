package org.healthadvicegroup.account;

import com.google.gson.Gson;
import lombok.Getter;
import org.bson.Document;
import org.healthadvicegroup.database.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitnessTracker implements Serializable<FitnessTracker> {

    // Stores user entries into their fitness tracker
    private final HashMap<String, List<FitnessTrackerEntry>> entries = new HashMap<>();
    private final UserAccount parent;

    // Constructor for when a fitness tracker is deserialized
    public FitnessTracker(UserAccount parent, Document document) {
        this.parent = parent;

        // deserialize fitness tracker entries
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            List<Document> entries = document.getList(entry.getKey(), Document.class);
            if (entries == null) {
                continue;
            }
            List<FitnessTrackerEntry> trackerEntries = new ArrayList<>();
            for (Document trackerEntryDocument : entries) {
                trackerEntries.add(new FitnessTrackerEntry(trackerEntryDocument));
            }
            this.entries.put(entry.getKey(), trackerEntries);
        }
    }

    // Constructor for when a new fitness tracker is created
    public FitnessTracker(UserAccount parent) {
        this.parent = parent;
    }

    @Override
    public Document toDocument(FitnessTracker object, Gson gson) {
        Document document = new Document();
        FitnessTrackerEntry serializationDummy = new FitnessTrackerEntry();

        // iterate over tracker entries and serialize to the document
        for (Map.Entry<String, List<FitnessTrackerEntry>> entry : object.entries.entrySet()) {
            document.append(entry.getKey(), serializationDummy.toDocumentList(entry.getValue()));
        }

        return document;
    }
}

@Getter
class FitnessTrackerEntry implements Serializable<FitnessTrackerEntry> {

    private final long time;
    private final double value;

    public FitnessTrackerEntry(Document document) {
        this.time = document.getLong("time");
        this.value = document.getDouble("value");
    }

    public FitnessTrackerEntry(double value) {
        this.time = System.currentTimeMillis();
        this.value = value;
    }

    // Empty constructor to easily get an instance of this object to access serialization methods
    FitnessTrackerEntry() {
        this(0);
    }

    @Override
    public Document toDocument(FitnessTrackerEntry object, Gson gson) {
        return new Document()
                .append("time", object.time)
                .append("value", object.value);
    }
}
