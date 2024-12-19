CREATE TABLE images (
                       id SERIAL PRIMARY KEY,
                       url VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE slideshows (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL
);


CREATE TABLE slides (
                       id SERIAL PRIMARY KEY,
                       slideshow_id BIGINT NOT NULL,
                       image_id BIGINT NOT NULL,
                       duration INT NOT NULL,
                       created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                       FOREIGN KEY (slideshow_id) REFERENCES slideshows(id) ON DELETE CASCADE,
                       FOREIGN KEY (image_id) REFERENCES images(id) ON DELETE CASCADE
);

CREATE INDEX idx_slide_slideshow_id ON slides(slideshow_id);
CREATE INDEX idx_slide_image_id ON slides(image_id);
CREATE INDEX idx_slide_order_index ON slides(created_timestamp);

CREATE TABLE proof_of_play (
                               id SERIAL PRIMARY KEY,
                               slideshow_id BIGINT NOT NULL,
                               image_id BIGINT NOT NULL,
                               event_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               FOREIGN KEY (slideshow_id) REFERENCES slideshows(id) ON DELETE CASCADE,
                               FOREIGN KEY (image_id) REFERENCES images(id) ON DELETE CASCADE
);

CREATE INDEX idx_proof_of_play_slideshow_id ON proof_of_play(slideshow_id);
CREATE INDEX idx_proof_of_play_image_id ON proof_of_play(image_id);
CREATE INDEX idx_proof_of_play_timestamp ON proof_of_play(event_timestamp);