-- Table for storing hourly usage data
CREATE TABLE usage_data (
    hour TIMESTAMP NOT NULL PRIMARY KEY,
    community_produced DOUBLE PRECISION NOT NULL DEFAULT 0,
    community_used DOUBLE PRECISION NOT NULL DEFAULT 0,
    grid_used DOUBLE PRECISION NOT NULL DEFAULT 0
);

-- Table for storing current percentage for the current hour
CREATE TABLE percentage_data (
    hour TIMESTAMP NOT NULL PRIMARY KEY,
    community_depleted DOUBLE PRECISION NOT NULL,  -- in percent (0.0 - 100.0)
    grid_portion DOUBLE PRECISION NOT NULL         -- in percent (0.0 - 100.0)
);